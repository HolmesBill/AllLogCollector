package com.holmesye.logcollector

import android.app.Application

/**
 * @author yejj
 * @date 2020/11/10
 * @Description :
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        LogcatHelper.getInstance().init(applicationContext)
        LogcatHelper.getInstance().tagsFilter(mutableListOf("holmesye","MainActivity"))
        LogcatHelper.getInstance().start()
    }
}