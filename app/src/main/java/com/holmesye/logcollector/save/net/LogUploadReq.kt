package com.holmesye.logcollector.save.net

/**
 * @author yejj
 * @date 2020/11/24
 * @Description :
 */
data class LogUploadReq(
    var userId: String? = null,
    var logList: MutableList<LogReqBody> = mutableListOf()
)