package com.holmesye.logcollector;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.holmesye.logcollector.utils.DateUtil;
import com.holmesye.logcollector.utils.FileSizeUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LogcatHelper {
    private Context mContext;

    private static LogcatHelper mInstance;

    private List<String> tags = new ArrayList<>();

    private List<LogDumper> logDumperList = new ArrayList<>();

    private ConcurrentHashMap<String, LogBean> logMap = new ConcurrentHashMap<>();

    /**
     * 例子："logcat holmesye:V MainActivity:V *:S | grep com.holmesye.logcollector"
     */

    public static LogcatHelper getInstance() {
        if (mInstance == null) {
            synchronized (LogcatHelper.class) {
                if (mInstance == null) {
                    mInstance = new LogcatHelper();
                }
            }
        }
        return mInstance;
    }

    private LogcatHelper() {
    }

    public void init(Context context) {
        mContext = context;
    }

    public void tagsFilter(@NonNull List<String> tags) {
//        StringBuilder sf = new StringBuilder();
//        sf.append("logcat");
//        for (String tag : tags) {
//            sf.append(" ").append(tag).append(":V");
//        }
//        sf.append(" *:S | grep ").append(mContext.getPackageName());
//        cmds = sf.toString();

        this.tags = tags;
    }

    public void start() {
        for (String tag : tags) {
            LogDumper dumper = new LogDumper(tag);
            logDumperList.add(dumper);
        }

        cleanLog();

//        //线程池启动
//        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
//        for (LogDumper dumper : logDumperList) {
//            cachedThreadPool.execute(dumper);
//        }

        ExecutorService cachedThreadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
                for (LogDumper dumper : logDumperList) {
            cachedThreadPool.execute(dumper);
        }
    }

    private void cleanLog() {
        //清除日志的缓存
        try {
            String cleanCMD = "logcat -c";
            Runtime.getRuntime().exec(cleanCMD);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
//        if (mLogDumper != null) {
//            mLogDumper.stopLogs();
//            mLogDumper = null;
//        }
    }

    /**
     * 一个线程启动一个logcat（暂定）
     */
    private class LogDumper implements Runnable {

        private String cmd;
        private String tag;

        private boolean mRunning = true;
        private BufferedReader mReader;

        public LogDumper(String tag) {
            this.tag = tag;
            initStart();
        }

        private void initStart() {
            try {
//                String cmds = "logcat | grep \"(" + pid + ")\"";//打印所有日志信息
//                String cmds = "logcat holmesye:V MainActivity:V *:S | grep com.holmesye.logcollector";//打印所有日志信息
//                String cmds = "logcat holmesye:V MainActivity:V *:S";//打印所有日志信息
//                String cmds = "logcat | find com.holmesye.logcollector";//打印所有日志信息

                cmd = "logcat" +
                        " " + tag + ":V" +
                        " *:S | grep " + mContext.getPackageName();
                Process logcatProc = Runtime.getRuntime().exec(cmd);
                mReader = new BufferedReader(new InputStreamReader(logcatProc.getInputStream()), 1024);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void stopLogs() {
            mRunning = false;
        }

        @Override
        public void run() {
            try {
                String line;
                while (mRunning && mReader != null && (line = mReader.readLine()) != null) {
                    if (line.length() == 0) {
                        continue;
                    }
//                    double size = FileSizeUtil.getFileOrFilesSize(file, FileSizeUtil.SIZETYPE_MB);
//                    if (size > MAX_SIZE) {
//                        LogcatHelper.this.stop();
//                        file.delete();
//                        LogcatHelper.this.start();
//                    }
                    if (line.contains(tag)) {
                        String time = DateUtil.getCurrentTime(DateUtil.DateFormatConstant.GL_TIME_FORMAT);
                        String logContent = line.split(tag)[1];
//                        System.out.println("log Content is " + logContent + "   time   " + time);
                        logMap.put(tag, new LogBean(logContent, time));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mReader != null) {
                    try {
                        mReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }


    }


}