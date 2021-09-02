package cn.roy.zlib.monitoring.bean;

import android.os.Parcel;

import java.io.Serializable;

/**
 * @Description 日志bean
 * @Author Roy Z
 * @Date 2018/4/16
 * @Version V1.0.0
 */
public class LogBean implements Serializable {
    public static final int VERBOSE = 0;
    public static final int DEBUG = 1;
    public static final int INFO = 2;
    public static final int WARN = 3;
    public static final int ERROR = 4;

    private int logLevel;
    private String logTag;
    private transient String logText;// 不需要序列化此字段
    private long date;

    public LogBean(int logLevel, String logTag, String logText) {
        this.logLevel = logLevel;
        this.logTag = logTag;
        this.logText = logText;

        this.date = System.currentTimeMillis();
    }

    public int getLogLevel() {
        return logLevel;
    }

    public String getLogLevelStr() {
        String s = "V";
        switch (logLevel) {
            case 1:
                s = "D";
                break;
            case 2:
                s = "I";
                break;
            case 3:
                s = "W";
                break;
            case 4:
                s = "E";
                break;
            default:
                break;
        }
        return s;
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    public String getLogTag() {
        return logTag;
    }

    public void setLogTag(String logTag) {
        this.logTag = logTag;
    }

    public String getLogText() {
        return logText;
    }

    public void setLogText(String logText) {
        this.logText = logText;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean containKey(String key) {
        return logTag.contains(key) || logText.contains(key);
    }

    protected LogBean(Parcel in) {
        this.logLevel = in.readInt();
        this.logTag = in.readString();
        this.logText = in.readString();
        this.date = in.readLong();
    }

}
