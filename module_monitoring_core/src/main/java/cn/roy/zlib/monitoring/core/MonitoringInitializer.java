package cn.roy.zlib.monitoring.core;

import android.content.Context;

import java.util.ServiceLoader;

/**
 * @Description: 日志监视窗初始化
 * @Author: Roy Z
 * @Date: 2021/08/12
 * @Version: v1.0
 */
public class MonitoringInitializer {
    private static MonitoringInitializer instance;

    public static MonitoringInitializer getInstance() {
        if (instance == null) {
            synchronized (MonitoringInitializer.class) {
                if (instance == null) {
                    instance = new MonitoringInitializer();
                }
            }
        }
        return instance;
    }

    private MonitoringAbility ability;
    private Context context;
    private int maxLogCount;
    private boolean enable;

    // 私有构造方法
    private MonitoringInitializer() {
    }

    public void init(MonitoringConfig initializer) {
        this.context = initializer.getContext().getApplicationContext();
        this.maxLogCount = initializer.getMaxLogCount();
        this.enable = initializer.isEnable();
    }

    public Context getContext() {
        return context;
    }

    public int getMaxLogCount() {
        return maxLogCount;
    }

    public boolean isEnable() {
        return enable;
    }

    /**
     * SPI机制实现获取注册的服务提供者
     *
     * @return
     */
    public MonitoringAbility getAbility() {
        if (ability == null) {
            ServiceLoader<MonitoringAbility> monitoringAbilities =
                    ServiceLoader.load(MonitoringAbility.class);
            if (monitoringAbilities != null) {
                ability = monitoringAbilities.iterator().next();
            }
        }
        return ability;
    }

    /**
     * 动态修改监视功能是否可用
     *
     * @param enable
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

}
