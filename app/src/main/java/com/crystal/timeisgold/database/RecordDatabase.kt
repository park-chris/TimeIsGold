package com.crystal.timeisgold.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.crystal.timeisgold.Record

@Database(entities = [Record::class], version = 1)
@TypeConverters(RecordTypeConverter::class)
abstract class RecordDatabase: RoomDatabase() {

    abstract fun RecordDao(): RecordDao
}