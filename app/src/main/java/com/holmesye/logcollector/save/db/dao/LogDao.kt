package com.holmesye.logcollector.save.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface LogDao {

    @Insert
    fun save(log: MutableList<LogEntity>)

    @Query("select * from log_DB where upLoadStatus = :upLoadStatus")
    fun selectByUploadStatus(upLoadStatus: String): MutableList<LogEntity>

    @Update
    fun updateById(log: MutableList<LogEntity>)
}