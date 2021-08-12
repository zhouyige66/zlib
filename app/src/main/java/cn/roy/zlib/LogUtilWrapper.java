package cn.roy.zlib;

import cn.roy.zlib.log.LogUtil;
import cn.roy.zlib.monitoring.core.ILog;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2021/07/22
 * @Version: v1.0
 */
public class LogUtilWrapper {

    public static void d(String msg) {
        ILog.d("roy", msg);
        LogUtil.d(msg);
    }

    public static void i(String msg) {
        ILog.i("roy", msg);
        LogUtil.i(msg);
    }

    public static void w(String msg) {
        ILog.w("roy", msg);
        LogUtil.w(msg);
    }

    public static void e(String msg) {
        ILog.e("roy", msg);
        LogUtil.e(msg);
    }
}
