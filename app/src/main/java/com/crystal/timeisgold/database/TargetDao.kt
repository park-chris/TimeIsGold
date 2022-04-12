package com.crystal.timeisgold.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.crystal.timeisgold.Target
import java.util.*

@Dao
interface TargetDao {

    @Query("SELECT * FROM target ORDER BY date DESC")
    fun getTargets(): LiveData<List<Target>>

    @Query("SELECT * FROM target WHERE id=(:id)")
    fun getTarget(id: UUID): LiveData<Target?>

    @Insert
    fun addTarget(target: Target)
}