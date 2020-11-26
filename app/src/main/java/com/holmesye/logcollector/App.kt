package com.holmesye.logcollector

import android.app.Application
import com.holmesye.logcollector.handler.ExceptionCollector
import com.holmesye.logcollector.handler.LogCollector
import com.holmesye.logcollector.save.db.DefaultLogInDBTaskBase
import com.holmesye.logcollector.save.net.DefaultExceptionSaveNetTask
import com.holmesye.logcollector.save.net.DefaultLogInNetTaskBase

/**
 * @author yejj
 * @date 2020/11/10
 * @Description :
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
//        LogcatHelper.getInstance().init(applicationContext)
//        LogcatHelper.getInstance().tagsFilter(mutableListOf("holmesye","MainActivity"))
//        LogcatHelper.getInstance().start()
//
//        LogCollector.init(applicationContext)
//            .tagsFilter(mutableListOf("holmesye", "MainActivity"))
//            .setOperations( mutableListOf(
//                DefaultLogInDBTaskBase(this),
//                DefaultLogInNetTaskBase(this)
//            ))
//            .start()

//        ExceptionCollector.init().handler(DefaultExceptionSaveNetTask(this))
    }
}