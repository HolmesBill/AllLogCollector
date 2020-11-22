package com.holmesye.logcollector.save

import android.content.Context
import com.holmesye.logcollector.LogBean
import com.holmesye.logcollector.save.dao.LogDataBase
import com.holmesye.logcollector.save.dao.LogEntity

/**
 * @author yejj
 * @date 2020/11/20
 * @Description : 默认的保存在数据库
 */
class DefaultLogSaveInDB(var mContext: Context) : LogcatSaveHandler {

    override fun save(logcatList: MutableList<LogBean>) {
        //用Room
        val logDao = LogDataBase.getDataBase(mContext).logDao()
        logcatList.forEach{
            logDao.save(LogEntity(it.logContent,it.logTime,it.tag,it.logType))
        }
    }
}