package com.github.redlann.firebase_extended

import androidx.lifecycle.LiveData
import com.github.redlann.networking.DataRequest
import com.github.redlann.networking.Failure
import com.github.redlann.networking.Success
import com.google.firebase.database.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

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
                val list = mutableListOf<T>()
                data.children.asFlow().mapNotNull {
                    it.getValue(type)
                }.toList(list)
                postValue(Success(list))
            }
        }
    }

    override fun onActive() {
        query.addValueEventListener(this)
    }

    override fun onInactive() {
        scope.coroutineContext.cancelChildren()
        query.removeEventListener(this)
    }
}