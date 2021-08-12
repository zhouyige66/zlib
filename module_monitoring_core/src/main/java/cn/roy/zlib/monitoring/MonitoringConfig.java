package cn.roy.zlib.monitoring;

import android.content.Context;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2021/08/12
 * @Version: v1.0
 */
public interface MonitoringConfig {

    Context getContext();

    int getMaxLogCount();

    boolean isEnable();

}
