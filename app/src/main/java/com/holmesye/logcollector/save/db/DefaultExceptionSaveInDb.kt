package com.holmesye.logcollector.save.db

import android.content.Context
import com.holmesye.logcollector.baseTask.BaseExceptionTask
import com.holmesye.logcollector.save.db.dao.LogDataBase
import com.holmesye.logcollector.save.db.dao.LogEntity

/**
 * @author yejj
 * @date 2020/11/25
 * @Description :
 */

class DefaultExceptionSaveInDb(private var mContext: Context) :BaseExceptionTask {
    override fun handler(exceptionMsg: String) {
        val logDao = LogDataBase.getDataBase(mContext).logDao()

        logDao.save(LogEntity(exceptionMsg,"2020年11月25日16:36:05","exception","exception"))
        println("保存成功")
    }
}