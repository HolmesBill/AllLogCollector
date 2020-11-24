package com.holmesye.logcollector.save.db

import android.content.Context
import com.holmesye.logcollector.LogBean
import com.holmesye.logcollector.save.LogcatSaveHandler
import com.holmesye.logcollector.save.db.dao.LogDataBase
import com.holmesye.logcollector.save.db.dao.LogEntity

/**
 * @author yejj
 * @date 2020/11/20
 * @Description : 默认的保存在数据库
 */
class DefaultLogSaveInDB(private var mContext: Context) : LogcatSaveHandler {

    override fun save(logcatList: MutableList<LogBean>) {
        //用Room
        val logDao = LogDataBase.getDataBase(mContext).logDao()
//        logcatList.forEach{
//            logDao.save(LogEntity(it.logContent,it.logTime,it.tag,it.logType))
//        }

        logDao.save(bean2data(logcatList))
    }

    private fun bean2data(logcatList: MutableList<LogBean>): MutableList<LogEntity> {
        val dataList = mutableListOf<LogEntity>()
        logcatList.forEach{
            dataList.add(LogEntity(it.logContent,it.logTime,it.tag,it.logType))
        }
        return dataList
    }
}