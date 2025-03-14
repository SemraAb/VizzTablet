package com.samraa.vizztablet.utils

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class CombinedStateFlow<R>(
    vararg flows: MutableSharedFlow<*>,
    private val combine: (list: List<Any?>) -> R
) : MediatorLiveData<R>() {

    private val list: MutableList<Any?> = MutableList(flows.size) { null }

    init {
        for (i in flows.indices) {
            super.addSource(flows[i].asLiveData()) {
                list[i] = it
                value = combine(list)
            }
        }
    }
}