package com.holmesye.logcollector.save.net

import android.content.Context
import com.holmesye.logcollector.baseTask.BaseLogcatHandlerTask
import com.holmesye.logcollector.bean.LogBean
import com.holmesye.logcollector.save.db.dao.LogDataBase
import com.holmesye.logcollector.save.db.dao.LogEntity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * @author yejj
 * @date 2020/11/24
 * @Description :
 */
class DefaultLogInNetTaskBase(private var mContext: Context) : BaseLogcatHandlerTask {

    /**
     *
     * 1.读取数据库，uploadStatus为0的数据，
     * 2.上传，改变uploadStatus为1
     * 2.上传成功，改变uploadStatus为2
     *
     */
    override fun save(logcatList: MutableList<LogBean>) {

        val logDao = LogDataBase.getDataBase(mContext).logDao()
        val logList = logDao.selectByUploadStatus("0")
        if (logList.isEmpty()) {
            return
        }
        val reqBodyList: MutableList<LogReqBody> =
            data2bean(logList)

        //上传
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.52.93:8080/")
            .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
            .build()
        val call = retrofit.create(Api::class.java).upload(LogUploadReq("111", reqBodyList))
//        //必须用同步的方式进行阻塞
        if (call.execute().body()?.code == "1") {
            logList.forEach {
                it.upLoadStatus = "2"
            }
            logDao.updateById(logList)
        }
//        call.enqueue(object : Callback<LogUploadResp> {
//            override fun onResponse(call: Call<LogUploadResp>, response: Response<LogUploadResp>) {
//
//                response.body()?.let {
//                    if (it.code == "1") {
//                        logList.forEach {
//                            it.upLoadStatus = "2"
//                        }
//                        logDao.updateById(logList)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<LogUploadResp>, t: Throwable) {
//                Log.e(LogcatHelperSingleThread.TAG, "onFailure: upload falt", )
//            }
//
//        })
    }

    private fun data2bean(data: MutableList<LogEntity>): MutableList<LogReqBody> {

        val reqBodyList: MutableList<LogReqBody> =
            mutableListOf()
        data.forEach {
            reqBodyList.add(
                com.holmesye.logcollector.save.net.LogReqBody(
                    it.logContent,
                    it.logTag,
                    it.logTime,
                    it.logType
                )
            )
        }

        return reqBodyList

    }

}

