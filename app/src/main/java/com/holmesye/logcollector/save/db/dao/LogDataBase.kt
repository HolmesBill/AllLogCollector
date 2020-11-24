package com.holmesye.logcollector.save.db.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 2, entities = [LogEntity::class])
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
            )
                .fallbackToDestructiveMigration()//数据库更新时删除数据重新创建
                .build()
                .apply {
                    instance = this
                }
        }
    }
}