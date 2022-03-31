package com.crystal.timeisgold

import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class RecordViewModel: ViewModel() {

    val records = mutableListOf<Record>()

    init {
        for (i in 0 until 50) {
            val record = Record()
            record.item = "운동"
            record.durationTime = 3600
            record.memo = "$i Record "
        }

    }

/*    private val recordRepository = RecordRepository.get()

    val recordList = recordRepository.getRecords()

    fun addRecord(record: Record) {
        recordRepository.addRecord(record)
    }*/
}