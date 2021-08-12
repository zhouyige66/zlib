package cn.roy.zlib.monitoring;

import android.util.Log;

/**
 * @Description: 监视工具
 * @Author: Roy Z
 * @Date: 2021/07/23
 * @Version: v1.0
 */
public class MonitoringAbilityForOp implements MonitoringAbility {

    public void v(String tag, String msg) {
        Log.v(tag, msg);
    }

    public void v(String tag, String msg, Throwable throwable) {
        Log.v(tag, msg, throwable);
    }

    public void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public void d(String tag, String msg, Throwable throwable) {
        Log.d(tag, msg, throwable);
    }

    public void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    public void i(String tag, String msg, Throwable throwable) {
        Log.i(tag, msg, throwable);
    }

    public void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    public void w(String tag, String msg, Throwable throwable) {
        Log.w(tag, msg, throwable);
    }

    public void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public void e(String tag, String msg, Throwable throwable) {
        Log.e(tag, msg, throwable);
    }

}
