package com.holmesye.logcollector.save.net

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author yejj
 * @date 2020/11/24
 * @Description :
 */

interface Api {

    @POST("/log/upload")
    fun upload(@Body req: LogUploadReq): Call<LogUploadResp>
}