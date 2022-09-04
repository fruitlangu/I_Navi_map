package com.inavi.inavi_map.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.inavi.inavi_map.db.user.UserDao
import com.inavi.inavi_map.db.user.UserLocationEntity

@Database(
    entities = [UserLocationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun userDao(): UserDao
}