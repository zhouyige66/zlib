package cn.roy.zlib.tool;

import android.content.Context;

import cn.roy.zlib.tool.core.Recorder;

/**
 * @Description 悬浮窗监视日志SDK
 * @Author Roy Z
 * @Date 2018/4/16
 * @Version V1.0.0
 */
public final class MonitoringToolSDK {
    // 私有构造方法
    private MonitoringToolSDK() {
    }

    private static MonitoringToolSDK instance;

    public static MonitoringToolSDK getInstance() {
        if (instance == null) {
            synchronized (Recorder.class) {
                if (instance == null) {
                    instance = new MonitoringToolSDK();
                }
            }
        }
        return instance;
    }

    private int maxLogCount = 1000;
    private boolean isFloatLogEnable = BuildConfig.DEBUG;

    public void init(Context context) {
        Recorder.getInstance().setAppContext(context.getApplicationContext());
        Recorder.getInstance().setMaxLogItemCount(maxLogCount);
    }

    public int getMaxLogCount() {
        return maxLogCount;
    }

    public void setMaxLogCount(int maxLogCount) {
        this.maxLogCount = maxLogCount;
    }

    public boolean isFloatLogEnable() {
        return isFloatLogEnable;
    }

    public void setFloatLogEnable(boolean floatLogEnable) {
        isFloatLogEnable = floatLogEnable;
    }
}
