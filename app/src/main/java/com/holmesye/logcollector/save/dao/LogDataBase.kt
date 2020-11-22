package com.holmesye.logcollector.save.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [LogEntity::class])
abstract class LogDataBase : RoomDatabase() {
    abstract fun logDao(): LogDao

    companion object {
        private var instance: LogDataBase? = null

        @Synchronized
        fun getDataBase(context: Context): LogDataBase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(
                context.applicationContext,
                LogDataBase::class.java,
                "logDB"
            ).build().apply {
                instance = this
            }
        }
    }
}