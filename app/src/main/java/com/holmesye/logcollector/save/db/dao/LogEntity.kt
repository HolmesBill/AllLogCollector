package com.holmesye.logcollector.save.db.dao

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "log_DB")
data class LogEntity(
    var logContent: String?,
    var logTime: String?,
    var logTag: String?,
    var logType: String,
    var upLoadStatus: String = "0" //是否上传，0：未上传，1：上传中，2：上传完
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}