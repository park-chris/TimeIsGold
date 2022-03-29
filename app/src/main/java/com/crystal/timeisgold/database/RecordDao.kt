package com.crystal.timeisgold.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.crystal.timeisgold.Record
import java.util.*

@Dao
interface RecordDao {

    @Query("SELECT * FROM record")
    fun getRecords(): List<Record>

    @Query("SELECT * FROM record WHERE id=(:id)")
    fun getRecord(id: UUID): Record?

    @Update
    fun updateRecord(record: Record)

    @Insert
    fun addRecord(record: Record)
}