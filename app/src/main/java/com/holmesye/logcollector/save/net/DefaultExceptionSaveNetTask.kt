package com.holmesye.logcollector.save.net

import android.content.Context
import com.holmesye.logcollector.baseTask.BaseExceptionTask
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author yejj
 * @date 2020/11/25
 * @Description :
 */
class DefaultExceptionSaveNetTask(var mContext: Context) : BaseExceptionTask {
    override fun handler(exceptionMsg: String) {


        //上传
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.52.93:8080/")
            .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
            .build()
        val call = retrofit.create(Api::class.java).upload(
            LogUploadReq(
                "111",
                mutableListOf(
                    LogReqBody(
                        exceptionMsg,
                        "exception",
                        "2020年11月25日17:41:04",
                        "exception"
                    )
                )
            )
        )
//        //必须用同步的方式进行阻塞
//        if (call.execute().body()?.code == "1") {
//            println("上传成功")
//        }else{
//            println("上传失败")
//        }
        call.enqueue(object : Callback<LogUploadResp> {
            override fun onResponse(call: Call<LogUploadResp>, response: Response<LogUploadResp>) {
                println("上传成功")
            }

            override fun onFailure(call: Call<LogUploadResp>, t: Throwable) {
                println("上传失败")
            }

        })
    }
}