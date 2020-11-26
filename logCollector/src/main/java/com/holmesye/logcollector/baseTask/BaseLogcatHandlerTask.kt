package com.holmesye.logcollector.baseTask

import com.holmesye.logcollector.bean.LogBean

/**
 * @author yejj
 * @date 2020/11/20
 * @Description : 处理logcat的任务类
 */
interface BaseLogcatHandlerTask {
    fun save(logcatList: MutableList<LogBean>)
}