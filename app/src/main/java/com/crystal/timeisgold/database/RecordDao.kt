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

    @Update
    fun updateRecord(record: Record)

    @Insert
    fun addRecord(record: Record)

    @Delete
    fun deleteRecord(record: Record)
}