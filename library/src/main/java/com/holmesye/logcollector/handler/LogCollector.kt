package com.holmesye.logcollector.handler

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.holmesye.logcollector.baseTask.BaseLogcatHandlerTask
import com.holmesye.logcollector.bean.LogBean
import com.holmesye.logcollector.utils.TimeUtils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @author yejj
 * @date 2020/11/19
 * @Description :
 */
object LogCollector {
    private var mContext: Context? = null
    private var matchTagList: List<String> = ArrayList()
    private var logList = mutableListOf<LogBean>()

    private var logCollectingThreadPool: ThreadPoolExecutor? = null //线程池：只做日志收集
    private var mTaskScheduledThreadPool: ScheduledThreadPoolExecutor? = null //处理操作线程池

    private var mTaskListBase: MutableList<BaseLogcatHandlerTask> = mutableListOf()

    private var logDumper: LogDumper? = null

    var TAG = "logcat"
    private var withoutMatchTagList = mutableListOf(TAG)

    private var isTaskRunning = true
    private var taskDelay = 1000L
    private var taskDelayTimeUnit = TimeUnit.MILLISECONDS

    fun setOperations(taskList: MutableList<BaseLogcatHandlerTask>): LogCollector {
        this.mTaskListBase = taskList
        return this
    }

    fun setWithoutMatchTags(tags: MutableList<String>): LogCollector {
        withoutMatchTagList.addAll(tags)
        return this
    }

    fun setTaskDelay(timeDelay: Long, timeDelayTimeUnit: TimeUnit): LogCollector {
        this.taskDelay = timeDelay
        this.taskDelayTimeUnit = timeDelayTimeUnit
        return this
    }

    fun init(context: Context?): LogCollector {
        mContext = context
        initThreadPool()
        return this
    }

    fun tagsFilter(tags: List<String>): LogCollector {
        this.matchTagList = tags
        return this
    }

    fun start() {
        logDumper = LogDumper(initCmd(matchTagList))

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
            taskDelay,
            taskDelayTimeUnit
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
//                println("开始执行任务集")
            val indexList = mutableListOf<LogBean>()
            synchronized(logList) {
                indexList.addAll(logList)
                logList.clear()
            }
            operationList.forEachIndexed {index,it->
                Log.d(TAG, "-------------------------开始任务$index ------------------------------")
                it.save(indexList)
                Log.d(TAG, "-------------------------任务$index 结束------------------------------")
            }
        }
    }

    /**
     * 利用线程收集日志
     */
    class LogDumper(private val cmd: String) : Runnable {
        private var mRunning = true
        private var mReader: BufferedReader? = null

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
                        val lineByPattern: Matcher =
                            Pattern.compile("[D,E,I,V,W] ([a-zA-Z0-9._-]*)")
                                .matcher(line) // 指定要匹配的字符串
                        if (lineByPattern.find()) {
                            val tagAndType = lineByPattern.group()
                            val tagAndTypeList = tagAndType.split(" ")
                            var tag: String
                            var type: String
                            var content: String
                            if (tagAndTypeList.size == 2) {
                                type = tagAndTypeList[0]
                                tag = tagAndTypeList[1]
                                //把自己TAG相关的日志去掉
                                if (tag == TAG) {
                                    continue
                                }
                                //用tag分割
                                content = line.replace(" ".toRegex(), "").split("$tag:")[1]
                                if (content.isEmpty()) {
                                    //如果content为空 有可能分割有问题，也有可能content就是空的，直接下一行
                                    continue
                                }
                                val logTime = TimeUtils.getNowTime()
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
        isTaskRunning = false
        mTaskScheduledThreadPool?.shutdown()
    }

}