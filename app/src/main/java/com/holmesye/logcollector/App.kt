package com.holmesye.logcollector

import android.app.Application
import com.holmesye.logcollector.save.db.DefaultLogInDBTask
import com.holmesye.logcollector.save.net.DefaultLogInNetTask

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
        LogcatHelperSingleThread.init(applicationContext)
//            .tagsFilter(mutableListOf("holmesye", "MainActivity"))
            .setOperations( mutableListOf(
                DefaultLogInDBTask(this),
                DefaultLogInNetTask(this)
            ))
            .start()
    }
}