package com.holmesye.logcollector;

/**
 * @author yejj
 * @date 2020/11/17
 * @Description : log 的bean类
 */
public class LogBean {
    public LogBean(String logContent, String logTime) {
        this.logContent = logContent;
        this.logTime = logTime;
    }

    private String logContent;
    private String logTime;

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
                '}';
    }
}
