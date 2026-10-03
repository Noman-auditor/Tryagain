package com.noratunnel.data.db
import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [ServerProfileEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun serverDao(): ServerProfileDao
}
