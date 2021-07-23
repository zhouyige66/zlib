package cn.roy.zlib.tool;

import android.content.Context;

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
            synchronized (MonitoringToolSDK.class) {
                if (instance == null) {
                    instance = new MonitoringToolSDK();
                }
            }
        }
        return instance;
    }

    private int maxLogCount = 1000;
    private boolean isFloatLogEnable = false;

    public void init(Context context) {

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
