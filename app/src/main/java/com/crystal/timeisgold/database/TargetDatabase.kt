package com.crystal.timeisgold.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.crystal.timeisgold.Target
import java.util.*

@Database(entities = [Target::class], version = 2)
@TypeConverters(TargetTypeConverter::class)
abstract class TargetDatabase: RoomDatabase() {

    abstract fun TargetDao(): TargetDao
}

val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE Target ADD COLUMN duration INTEGER NOT NULL DEFAULT 0"
        )
        database.execSQL(
            "ALTER TABLE Target ADD COLUMN targetTime INTEGER NOT NULL DEFAULT 0"
        )
        database.execSQL(
            "ALTER TABLE Target ADD COLUMN timeOver INTEGER NOT NULL DEFAULT 0"
        )
    }

}
