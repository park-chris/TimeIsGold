package com.crystal.timeisgold.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.crystal.timeisgold.Record

@Database(entities = [Record::class], version = 2)
@TypeConverters(RecordTypeConverter::class)
abstract class RecordDatabase: RoomDatabase() {

    abstract fun RecordDao(): RecordDao
}

val migration_1_2 = object : Migration(1,2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE Record ADD COLUMN created TEXT NOT NULL DEFAULT ''"
        )
    }

}