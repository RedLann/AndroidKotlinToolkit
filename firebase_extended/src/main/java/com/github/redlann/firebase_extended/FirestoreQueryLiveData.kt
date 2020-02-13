package com.github.redlann.firebase_extended

import android.os.Handler
import androidx.lifecycle.LiveData
import com.github.redlann.networking.DataRequest
import com.github.redlann.networking.Failure
import com.github.redlann.networking.Success
import com.google.firebase.firestore.*

class FirestoreQueryLiveData<T : DTO>(
    private val query: Query,
    private val type: Class<T>
) :
    LiveData<DataRequest<List<T>>>(),
    EventListener<QuerySnapshot> {

    private var listenerRemovePending = false
    private val handler = Handler()

    private var registration: ListenerRegistration? = null

    override fun onEvent(snapshots: QuerySnapshot?, e: FirebaseFirestoreException?) {
        if (e != null) {
            value = Failure(e)
            return
        }
        value = Success(documentToList(snapshots!!))
    }

    private val removeListener = Runnable {
        registration!!.remove()
        registration = null
        listenerRemovePending = false
    }

    override fun onActive() {
        super.onActive()

        if (listenerRemovePending) {
            handler.removeCallbacks(removeListener)
        } else {
            if (registration == null) {
                registration = query.addSnapshotListener(this)
            }
        }

        listenerRemovePending = false
    }

    override fun onInactive() {
        super.onInactive()
        // Listener removal is schedule on a edit_project_menu second delay
        // This is to save on counts against the quota or the bill of Firebase :)
        handler.postDelayed(removeListener, 2000)
        listenerRemovePending = true
    }


    private fun documentToList(snapshots: QuerySnapshot): List<T> {
        if (snapshots.isEmpty) {
            return emptyList()
        }

        return snapshots.documents.map {
            it.toObject(type)!!.withId<T>(it.id)
        }
    }
}