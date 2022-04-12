package com.crystal.timeisgold

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.crystal.timeisgold.database.TargetDatabase
import com.crystal.timeisgold.database.migration_1_2
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors


private const val DATABASE_NAME = "target-database"

class TargetRepository private constructor(context: Context){

    private val database: TargetDatabase = Room.databaseBuilder(
        context.applicationContext,
        TargetDatabase::class.java,
        DATABASE_NAME
    ).addMigrations(migration_1_2).build()

    private val targetDao = database.TargetDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getTargets(): LiveData<List<Target>> = targetDao.getTargets()
    fun getTarget(id: UUID): LiveData<Target?> = targetDao.getTarget(id)

    fun addTarget(target: Target) {
        executor.execute {
            targetDao.addTarget(target)
        }
    }

    companion object {

        private var INSTANCE: TargetRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = TargetRepository(context)
            }
        }

        fun get():TargetRepository {
            return INSTANCE ?: throw IllegalStateException("TargetRepository must be initialized")
        }
    }
}