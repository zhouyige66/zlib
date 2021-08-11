package cn.roy.zlib;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.roy.zlib.log.LogConfigBuilder;
import cn.roy.zlib.log.LogUtil;
//import cn.roy.zlib.monitor.Monitor;
import cn.roy.zlib.tool.MonitoringToolSDK;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/5/12 17:12
 * @Version: v1.0
 */
public class MainApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        Class<?> trace = null;
        try {
            trace = Class.forName("android.os.Trace");
            Method setAppTracingAllowed = trace.getDeclaredMethod("setAppTracingAllowed", boolean.class);
            setAppTracingAllowed.invoke(null, true);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        new LogConfigBuilder(this).buildDefault();
//        Monitor.getInstance().init(new Monitor.Options(this)
//                .setBlockMonitorEnable(true)
//                .setBlockMonitorTimeout(2000)
//                .setCrashMonitorEnable(true)
//                .setCrashLogAutoSave(true)
//                .setExceptionInfoLogger(exceptionInfo -> LogUtil.e(exceptionInfo)));
        MonitoringToolSDK.getInstance().init(this);
    }

}
