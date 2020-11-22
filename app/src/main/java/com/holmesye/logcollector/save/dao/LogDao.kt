package com.holmesye.logcollector.save.dao

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface LogDao{

    @Insert
    fun save(log:LogEntity)
}