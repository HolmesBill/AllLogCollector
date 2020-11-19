package com.holmesye.logcollector;

/**
 * @author yejj
 * @date 2020/11/17
 * @Description : log 的bean类
 */
public class LogBean {
    public LogBean(String logContent, String logTime, String logType, String tag) {
        this.logContent = logContent;
        this.logTime = logTime;
        this.logType = logType;
        this.tag = tag;
    }

    private String logContent;
    private String logTime;
    private String logType;
    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    @Override
    public String toString() {
        return "LogBean{" +
                "logContent='" + logContent + '\'' +
                ", logTime='" + logTime + '\'' +
                ", logType='" + logType + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
