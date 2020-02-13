package com.github.redlann.firebase_extended

import androidx.lifecycle.LiveData
import com.github.redlann.networking.DataRequest
import com.github.redlann.networking.Failure
import com.github.redlann.networking.Success
import com.google.firebase.firestore.*

interface DTO {
    var key: String

    fun <T : DTO> withId(id: String): T {
        this.key = id
        return this as T
    }
}

class DocumentLiveData<T : DTO>(private val ref: DocumentReference, private val type: Class<T>) : LiveData<DataRequest<T>>(),
    EventListener<DocumentSnapshot> {
    var registration: ListenerRegistration? = null

    override fun onActive() {
        super.onActive()
        registration = ref.addSnapshotListener(this)
    }

    override fun onInactive() {
        super.onInactive()
        if (registration != null) {
            registration?.remove()
            registration = null
        }
    }

    override fun onEvent(snapshot: DocumentSnapshot?, e: FirebaseFirestoreException?) {
        e?.let {
            value = Failure(it)
            return
        }
        value = Success(snapshot?.toObject(type)!!.withId<T>(snapshot.id))
    }
}