package com.crystal.timeisgold.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.crystal.timeisgold.Record
import java.util.*

@Dao
interface RecordDao {

    @Query("SELECT * FROM record ORDER BY date DESC")
    fun getRecords(): LiveData<List<Record>>

    @Query("SELECT * FROM record WHERE id=(:id)")
    fun getRecord(id: UUID): LiveData<Record?>

    @Query("SELECT sum(durationTime) FROM record WHERE created = date('now', 'localtime') AND item=(:item)")
    fun getTime(item: String): Int

    @Query("SELECT * FROM record WHERE created BETWEEN date('now', 'start of day', '-8 days') AND date('now', 'start of day', '-1 day') ORDER BY date DESC")
    fun getDailyTime(): LiveData<List<Record>>


    @Update
    fun updateRecord(record: Record)

    @Insert
    fun addRecord(record: Record)

    @Delete
    fun deleteRecord(record: Record)
}