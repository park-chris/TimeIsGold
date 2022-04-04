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

    fun convertTimestampToDate(date: Date): String {
        val pattern = SimpleDateFormat("yyyy.MM.dd EEEE")
        return pattern.format(date)
    }

    fun convertTimestampToTime(date: Date): String {
        val pattern = SimpleDateFormat("HH:mm:ss")
        return pattern.format(date)
    }

    fun getEndTime(date: Date, time: Int): String {
        val hPattern = SimpleDateFormat("HH")
        val mPattern = SimpleDateFormat("mm")
        val sPattern = SimpleDateFormat("ss")
        val decimalPattern = DecimalFormat("00")
        var hour = hPattern.format(date).toInt()
        var min = mPattern.format(date).toInt()
        var sec = sPattern.format(date).toInt()

        val endTime = hour * 3600 + min * 60 + sec + time

        if (endTime >= 3600) {
            hour = endTime / 3600
            val extra = endTime % 3600
            min = extra / 60
            sec = extra % 60

        } else {
            hour = 0
            min = endTime / 60
            sec = endTime % 60
        }

        return "${decimalPattern.format(hour)}:${decimalPattern.format(min)}:${decimalPattern.format(sec)}"

    }

    fun getDurationTime(time: Int): String {
        val decimalPattern = DecimalFormat("00")
        val hour : Int
        val min : Int
        val sec : Int

        if (time >= 3600) {
            hour = time / 3600
            val extra = time % 3600
            min = extra / 60
            sec = extra % 60

        } else {
            hour = 0
            min = time / 60
            sec = time % 60
        }

        return "${decimalPattern.format(hour)}:${decimalPattern.format(min)}:${decimalPattern.format(sec)}"

    }

}