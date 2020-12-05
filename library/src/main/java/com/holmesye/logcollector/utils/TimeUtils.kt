package com.holmesye.logcollector.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author yejj
 * @date 2020/11/26
 * @Description :
 */
object TimeUtils {

    @SuppressLint("SimpleDateFormat")
    fun getNowTime(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").apply {
            timeZone = TimeZone.getTimeZone("Asia/Shanghai")
        }.format(Date())
    }
}