package cn.roy.zlib.monitoring;

import android.util.Log;

import cn.roy.zlib.monitoring.bean.LogBean;
import cn.roy.zlib.monitoring.core.MonitoringAbility;
import cn.roy.zlib.monitoring.core.MonitoringInitializer;
import cn.roy.zlib.monitoring.core.Recorder;

/**
 * @Description: 监视工具
 * @Author: Roy Z
 * @Date: 2021/06/25
 * @Version: v1.0
 */
public class MonitoringAbilityForDebug implements MonitoringAbility {

    public void v(String tag, String msg) {
        addLog(LogBean.VERBOSE, tag, msg);
    }

    public void v(String tag, String msg, Throwable throwable) {
        addLog(LogBean.VERBOSE, tag, msg + '\n' + Log.getStackTraceString(throwable));
    }

    public void d(String tag, String msg) {
        addLog(LogBean.DEBUG, tag, msg);
    }

    public void d(String tag, String msg, Throwable throwable) {
        addLog(LogBean.DEBUG, tag, msg + '\n' + Log.getStackTraceString(throwable));
    }

    public void i(String tag, String msg) {
        addLog(LogBean.INFO, tag, msg);
    }

    public void i(String tag, String msg, Throwable throwable) {
        addLog(LogBean.INFO, tag, msg + '\n' + Log.getStackTraceString(throwable));
    }

    public void w(String tag, String msg) {
        addLog(LogBean.WARN, tag, msg);
    }

    public void w(String tag, String msg, Throwable throwable) {
        addLog(LogBean.WARN, tag, msg + '\n' + Log.getStackTraceString(throwable));
    }

    public void e(String tag, String msg) {
        addLog(LogBean.ERROR, tag, msg);
    }

    public void e(String tag, String msg, Throwable throwable) {
        addLog(LogBean.ERROR, tag, msg + '\n' + Log.getStackTraceString(throwable));
    }

    private void addLog(int level, String tag, String msg) {
        if (MonitoringInitializer.getInstance().isEnable()) {
            LogBean bean = new LogBean(level, tag, msg);
            Recorder.getInstance().addLog(bean);
        }
    }

}
