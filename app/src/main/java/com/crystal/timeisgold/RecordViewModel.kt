package com.crystal.timeisgold

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class RecordViewModel: ViewModel() {

    private val recordRepository = RecordRepository.get()
    private val recordIdLiveData = MutableLiveData<UUID>()

    val recordListLiveData = recordRepository.getRecords()

    var recordLiveDate: LiveData<Record?> = Transformations.switchMap(recordIdLiveData) {recordId ->
        recordRepository.getRecord(recordId)
    }

    fun loadRecord(recordId: UUID) {
        recordIdLiveData.value = recordId
    }

    fun saveRecord(record: Record) {
        recordRepository.updateRecord(record)
    }

    fun addRecord(record: Record) {
        recordRepository.addRecord(record)
    }

    fun deleteRecord(record: Record) {
        recordRepository.deleteRecord(record)
    }
}