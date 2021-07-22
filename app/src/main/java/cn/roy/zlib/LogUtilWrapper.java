package cn.roy.zlib;

import cn.roy.zlib.log.LogUtil;
import cn.roy.zlib.tool.MonitoringTool;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2021/07/22
 * @Version: v1.0
 */
public class LogUtilWrapper {

    public static void d(String msg) {
        MonitoringTool.d("roy", msg);
        LogUtil.d(msg);
    }

}
