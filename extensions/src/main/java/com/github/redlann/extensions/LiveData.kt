package com.github.redlann.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <OriginalType, MappedType> LiveData<OriginalType>.attachMediator(observer: (MediatorLiveData<MappedType>, OriginalType) -> Unit): MediatorLiveData<MappedType> {
    val mediator = MediatorLiveData<MappedType>()
    mediator.addSource(this) { observer(mediator, it) }
    return mediator
}

