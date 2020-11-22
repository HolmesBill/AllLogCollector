package com.holmesye.logcollector.save.dao

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class LogEntity(
    var logContent: String?,
    var time: String?,
    var tag: String?,
    var type: String,
    var isLoad: String = "0" //是否上传，0：未上传，1：上传中，2：上传完
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}