package com.github.redlann.firebase_extended

import androidx.lifecycle.LiveData
import com.github.redlann.networking.DataRequest
import com.github.redlann.networking.Failure
import com.github.redlann.networking.Success
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class RealtimeDocumentLiveData<T : DTO>(
    private val query: Query,
    private val type: Class<T>
) : LiveData<DataRequest<List<T>>>(), ValueEventListener {
    private val job = SupervisorJob()
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + job)

    override fun onCancelled(error: DatabaseError) {
        value = Failure(error.toException())
    }

    override fun onDataChange(data: DataSnapshot) {
        scope.launch(Dispatchers.IO) {
            val list = mutableListOf<T>()
            data.children.asFlow().mapNotNull {
                it.getValue(type)
            }.toList(list)
            postValue(Success(list))
        }
    }

    override fun onActive() {
        query.addValueEventListener(this)
    }

    override fun onInactive() {
        query.removeEventListener(this)
    }
}