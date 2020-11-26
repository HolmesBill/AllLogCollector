package com.holmesye.logcollector.handler

import android.os.Process
import com.holmesye.logcollector.baseTask.BaseExceptionTask

/**
 * @author yejj
 * @date 2020/11/25
 * @Description :
 */
object ExceptionCollector : Thread.UncaughtExceptionHandler {

    // 系统默认的UncaughtException处理类
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null

    private var exceptionTask: BaseExceptionTask? = null

    fun handler(exceptionTask: BaseExceptionTask) {
        this.exceptionTask = exceptionTask
    }

    fun init(): ExceptionCollector {
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        return this
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        handleException(e)
        //交给系统处理，如果系统不处理就自己杀死自己
        mDefaultHandler?.uncaughtException(t, e)?.run {
            Process.killProcess(Process.myPid());
        }
    }

    private fun handleException(e: Throwable) {
        val sb = StringBuffer()
        sb.append(e.message).append("\n")
        val stackTrace = e.stackTrace
        stackTrace.forEach {
            sb.append("at ${it.className}.${it.methodName} (${it.fileName}:${it.lineNumber})")
            sb.append("\n")
        }

        exceptionTask?.handler(sb.toString())
    }


}