package com.holmesye.logcollector.handler

import android.annotation.SuppressLint
import android.content.Context
import com.holmesye.logcollector.baseTask.BaseLogcatHandlerTask
import com.holmesye.logcollector.bean.LogBean
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @author yejj
 * @date 2020/11/19
 * @Description :
 */
object LogCollector {

    private var mContext: Context? = null
    private var tags: List<String> = ArrayList()
    private var logList = mutableListOf<LogBean>()

    private var logCollectingThreadPool: ThreadPoolExecutor? = null //线程池：只做日志收集
    private var mTaskScheduledThreadPool: ScheduledThreadPoolExecutor? = null //处理操作线程池

    private var mTaskListBase: MutableList<BaseLogcatHandlerTask> = mutableListOf()

    private var logDumper: LogDumper? = null

    public var TAG = "logcat"

    private var isTaskRunning = true

    fun setOperations(taskList: MutableList<BaseLogcatHandlerTask>): LogCollector {
        this.mTaskListBase = taskList
        return this
    }

    fun init(context: Context?): LogCollector {
        mContext = context
        initThreadPool()
        return this
    }

    fun tagsFilter(tags: List<String>): LogCollector {
        this.tags = tags
        return this
    }

    fun start() {
        logDumper = LogDumper(initCmd(tags))

        //每次启动都清除之前的日志
        cleanLog()
        //开始日志收集
        logCollectingThreadPool?.execute(logDumper)
        //写个定时操作机
        handlerLogcat()
    }

    /**
     * 初始化所有线程池
     */
    private fun initThreadPool() {
        logCollectingThreadPool = ThreadPoolExecutor(
            0, Int.MAX_VALUE,
            60L, TimeUnit.SECONDS,
            SynchronousQueue()
        )

        mTaskScheduledThreadPool = ScheduledThreadPoolExecutor(5)
    }

    private fun handlerLogcat() {
        if (mTaskListBase.isEmpty()) {
            return
        }

        mTaskScheduledThreadPool?.scheduleWithFixedDelay(
            HandlerDumper(mTaskListBase),
            1000,
            1000,
            TimeUnit.MILLISECONDS
        )
    }

    private fun initCmd(tags: List<String>): String {
        var cmd = "logcat "
        tags.forEach {
            cmd += "$it:V "
        }
        if (tags.isNotEmpty()) {
            cmd += "*:S "
        }
        cmd += "| grep ${mContext?.packageName}"
        return cmd
    }

    private fun cleanLog() {
        //清除日志的缓存
        try {
            val cleanCMD = "logcat -c"
            Runtime.getRuntime().exec(cleanCMD)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    class HandlerDumper(private var operationList: MutableList<BaseLogcatHandlerTask>) : Runnable {
        override fun run() {

            if (!isTaskRunning) {
                return
            }

            synchronized(logList) {
//                println("开始执行任务集")
                val indexList = mutableListOf<LogBean>()
                indexList.addAll(logList)
                logList.clear()
                operationList.forEach {
                    it.save(indexList)
                }
            }
        }
    }

    /**
     * 利用线程收集日志
     */
    class LogDumper(private val cmd: String) : Runnable {
        private var mRunning = true
        private var mReader: BufferedReader? = null

        @SuppressLint("SimpleDateFormat")
        override fun run() {
            var line = ""
            try {
                val logcatProcess = Runtime.getRuntime().exec(cmd)
                mReader = BufferedReader(InputStreamReader(logcatProcess.inputStream), 1024)
                while (mRunning) {

                    mReader?.let {
                        if (mReader != null) {
                            line = mReader!!.readLine()
                        }
                    }

                    if (line.isNotEmpty()) {
                        //处理logcat   例子  11-19 05:14:33.225 20634 20634 D holmesye: onCreate: 大厦上发的是打发士大夫
                        val patten = Pattern.compile("[D,E,I,V,W] ([a-zA-Z0-9._-]*)") //编译正则表达式
                        val matcher: Matcher = patten.matcher(line) // 指定要匹配的字符串
                        if (matcher.find()) {
                            val tagAndType = matcher.group()
                            val tagAndTypeList = tagAndType.split(" ")
                            var tag = ""
                            var type = ""
                            var content = ""
                            if (tagAndTypeList.size == 2) {
                                type = tagAndTypeList[0]
                                tag = tagAndTypeList[1]
                                //把自己TAG相关的日志去掉
                                if (tag == TAG) {
                                    continue
                                }
                                //用tag分割
                                content = line.replace(" ".toRegex(), "").split("$tag:")[1]
                                val logTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").apply {
                                    timeZone = TimeZone.getTimeZone("Asia/Shanghai")
                                }.format(Date())
                                val logBean = LogBean(content, logTime, type, tag)
//                                println(logBean)
                                logList.add(logBean)
                            }
                        }

                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                stopAll()
            } finally {
                if (mReader != null) {
                    try {
                        mReader!!.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }

        fun stop() {
            mRunning = false
        }
    }

    fun stopAll() {
        logDumper?.stop()
        mTaskScheduledThreadPool?.shutdown()
    }

}

