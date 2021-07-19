package cn.roy.zlib.monitor;

import android.content.Context;
import android.text.TextUtils;

import com.github.moduth.blockcanary.BlockCanary;

/**
 * @Description: 应用监控器
 * @Author: Roy Z
 * @Date: 2020/5/12 16:11
 * @Version: v1.0
 */
public final class Monitor {
    private volatile static Monitor instance;

    public static Monitor getInstance() {
        if (instance == null) {
            synchronized (Monitor.class) {
                if (instance == null) {
                    instance = new Monitor();
                }
            }
        }
        return instance;
    }

    private Monitor() {
    }

    public void init(Options options) {
        if (options.isCrashMonitorEnable) {
            // 记录崩溃日志
            CrashExceptionHandler handler = CrashExceptionHandler.getInstance();
            handler.init(options.applicationContext);
            handler.setAutoSaveCrashLog(options.autoSaveCrashLog);
            if (!TextUtils.isEmpty(options.crashMonitorLogPath)) {
                handler.setCrashLogPath(options.crashMonitorLogPath);
            }
            if (options.crashExceptionCustomHandler != null) {
                handler.setCrashExceptionCustomHandler(options.crashExceptionCustomHandler);
            }
            if (options.crashExceptionInfoLogger != null) {
                handler.setCrashExceptionInfoLogger(options.crashExceptionInfoLogger);
            }
        }
        if (options.isBlockMonitorEnable) {
            // 在主进程初始化调用哈
            AppBlockCanaryContext appBlockCanaryContext = new AppBlockCanaryContext();
            appBlockCanaryContext.setBlockLogPath(options.blockMonitorLogPath);
            appBlockCanaryContext.setBlockTimeout(options.blockMonitorTimeout);
            BlockCanary.install(options.applicationContext, appBlockCanaryContext).start();
        }
    }

    /**
     * 参数配置类
     */
    public static final class Options {
        private Context applicationContext;
        private boolean isCrashMonitorEnable = false;
        private String crashMonitorLogPath;// 崩溃日志存储路径
        private boolean autoSaveCrashLog = false;// 自动保存崩溃日志
        private CrashExceptionCustomHandler crashExceptionCustomHandler;// 用户自定义异常处理
        private CrashExceptionInfoLogger crashExceptionInfoLogger;// 用于记录崩溃日志信息
        private boolean isBlockMonitorEnable = false;
        private int blockMonitorTimeout;
        private String blockMonitorLogPath;

        public Options(Context context) {
            this.applicationContext = context.getApplicationContext();
        }

        public Options setCrashMonitorEnable(boolean crashMonitorEnable) {
            isCrashMonitorEnable = crashMonitorEnable;
            return this;
        }

        public Options setCrashMonitorLogPath(String crashMonitorLogPath) {
            this.crashMonitorLogPath = crashMonitorLogPath;
            return this;
        }

        public Options setCrashLogAutoSave(boolean autoSaveCrashLog) {
            this.autoSaveCrashLog = autoSaveCrashLog;
            return this;
        }

        public Options setCrashExceptionCustomHandler(CrashExceptionCustomHandler crashExceptionCustomHandler) {
            this.crashExceptionCustomHandler = crashExceptionCustomHandler;
            return this;
        }

        public Options setExceptionInfoLogger(CrashExceptionInfoLogger crashExceptionInfoLogger) {
            this.crashExceptionInfoLogger = crashExceptionInfoLogger;
            return this;
        }

        public Options setBlockMonitorEnable(boolean blockMonitorEnable) {
            isBlockMonitorEnable = blockMonitorEnable;
            return this;
        }

        public Options setBlockMonitorTimeout(int blockMonitorTimeout) {
            this.blockMonitorTimeout = blockMonitorTimeout;
            return this;
        }

        public Options setBlockMonitorLogPath(String blockMonitorLogPath) {
            this.blockMonitorLogPath = blockMonitorLogPath;
            return this;
        }
    }

}
