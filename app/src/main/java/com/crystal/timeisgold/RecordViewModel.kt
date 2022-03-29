package com.crystal.timeisgold

import androidx.lifecycle.ViewModel

class RecordViewModel: ViewModel() {

    private val recordRepository = RecordRepository.get()

    val recordList = recordRepository.getRecords()

    fun addRecord(record: Record) {
        recordRepository.addRecord(record)
    }
}