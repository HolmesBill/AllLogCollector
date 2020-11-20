package com.holmesye.logcollector.save

import com.holmesye.logcollector.LogBean

/**
 * @author yejj
 * @date 2020/11/20
 * @Description :
 */
interface LogcatSaveHandler {
    fun save(logcatList:MutableList<LogBean>)
}