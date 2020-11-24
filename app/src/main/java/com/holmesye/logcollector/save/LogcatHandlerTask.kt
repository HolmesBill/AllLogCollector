package com.holmesye.logcollector.save

import com.holmesye.logcollector.LogBean

/**
 * @author yejj
 * @date 2020/11/20
 * @Description : 处理logcat的任务类
 */
interface LogcatHandlerTask {
    fun save(logcatList: MutableList<LogBean>)
}