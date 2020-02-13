package com.squash.data.repository.datasource

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.*
import com.log4k.d
import com.squash.commons.DataRequest
import com.squash.commons.Failure
import com.squash.commons.Success
import com.squash.data.DTO
import com.squash.data.ProjectDTO
import com.squash.data.TagDTO
import com.squash.data.TodoDTO
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RealtimeDocumentListLiveData<T : DTO>(
    private val query: Query,
    private val type: Class<T>
) : LiveData<DataRequest<List<T>>>(), ValueEventListener {
    private var cacheValue: DataSnapshot? = null
    private val job = SupervisorJob()
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + job)

    override fun onCancelled(error: DatabaseError) {
        value = Failure(error.toException())
    }

    override fun onDataChange(data: DataSnapshot) {
        scope.launch(Dispatchers.IO) {
            if (cacheValue == null || cacheValue.toString() != data.toString()) {
                cacheValue = data
                val list = data.children.asFlow().mapNotNull { projectSnapshot ->
                    projectSnapshot.getValue(type)?.apply {
                        when (type) {
                            ProjectDTO::class.java -> {
                                this as ProjectDTO
                                parseProject(this, projectSnapshot)
                            }

                            TodoDTO::class.java -> {
                                this as TodoDTO
                                val tags =
                                    FirebaseDatabase.getInstance()
                                        .reference.currentUser()
                                        .child("tags")
                                        .once()
                                        .await()
                                        .children
                                        .asFlow()
                                        .mapNotNull {
                                            it.getValue(TagDTO::class.java)
                                                ?.withId<TagDTO>(it.key!!)
                                        }.toList()

                                this.tags = tags.filter {
                                    getTags(projectSnapshot.child("tags")).contains(it.key)
                                }

                            }
                        }
                    }
                }.toList()
                postValue(Success(list))
            }
        }
    }

    private suspend fun parseProject(project: ProjectDTO, snapshot: DataSnapshot): ProjectDTO {
        val todosSnapshot = snapshot.child("todos")
        if (todosSnapshot.exists()) {
            project.todos = todosSnapshot.children.asFlow().mapNotNull {
                parseTodo(it)
            }.toList()
        }
        return project
    }

    private fun parseTodo(snapshot: DataSnapshot): TodoDTO? {
        return snapshot.getValue(TodoDTO::class.java)
    }

    private fun getTags(snapshot: DataSnapshot): List<String> =
        if (snapshot.exists()) snapshot.children.mapNotNull { it.key } else emptyList()

    override fun onActive() {
        query.addValueEventListener(this)
    }

    override fun onInactive() {
        scope.coroutineContext.cancelChildren()
        query.removeEventListener(this)
    }
}


suspend fun DatabaseReference.once(): Deferred<DataSnapshot> = coroutineScope {
    async(Dispatchers.IO) {
        suspendCoroutine<DataSnapshot> { continuation ->
            addListenerForSingleValueEvent(FValueEventListener(
                onDataChange = { continuation.resume(it) },
                onError = { continuation.resumeWithException(it.toException()) }
            ))
        }
    }
}

suspend fun Query.once(): Deferred<DataSnapshot> = coroutineScope {
    async(Dispatchers.IO) {
        suspendCoroutine<DataSnapshot> { continuation ->
            addListenerForSingleValueEvent(FValueEventListener(
                onDataChange = { continuation.resume(it) },
                onError = { continuation.resumeWithException(it.toException()) }
            ))
        }
    }
}

class FValueEventListener(
    val onDataChange: (DataSnapshot) -> Unit,
    val onError: (DatabaseError) -> Unit
) : ValueEventListener {
    override fun onDataChange(data: DataSnapshot) = onDataChange.invoke(data)
    override fun onCancelled(error: DatabaseError) = onError.invoke(error)
}