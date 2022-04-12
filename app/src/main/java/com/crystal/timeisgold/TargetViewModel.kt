package com.crystal.timeisgold

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class TargetViewModel: ViewModel() {

    private val targetRepository = TargetRepository.get()
    private val targetIdLiveData = MutableLiveData<UUID>()

    val targetListLiveData = targetRepository.getTargets()

    var targetLiveData: LiveData<Target?> = Transformations.switchMap(targetIdLiveData) { targetId ->
        targetRepository.getTarget(targetId)
    }

    fun loadTarget(targetId: UUID) {
        targetIdLiveData.value = targetId
    }


    fun addTarget(target: Target) {
        targetRepository.addTarget(target)
    }

}