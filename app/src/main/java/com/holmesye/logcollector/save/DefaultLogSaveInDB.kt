package com.holmesye.logcollector.save

import com.holmesye.logcollector.LogBean

/**
 * @author yejj
 * @date 2020/11/20
 * @Description : 默认的保存在数据库
 */
class DefaultLogSaveInDB : LogcatSaveHandler {
    override fun save(logcatList: MutableList<LogBean>) {

    }
}