package cn.roy.zlib.monitoring.core;

import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.roy.zlib.monitoring.bean.LogBean;
import cn.roy.zlib.monitoring.bean.TagBean;
import cn.roy.zlib.monitoring.component.LogService;
import cn.roy.zlib.monitoring.util.AppOpsManagerUtil;

/**
 * @Description: 记录器
 * @Author: Roy Z
 * @Date: 2020/4/6 14:59
 * @Version: v1.0
 */
public final class Recorder {
    public static final String INTENT_FILTER_LOG_EVENT = "intent_filter_log_event";
    private static Recorder instance;

    public static Recorder getInstance() {
        if (instance == null) {
            synchronized (Recorder.class) {
                if (instance == null) {
                    instance = new Recorder();
                }
            }
        }
        return instance;
    }

    private List<LogBean> originData;
    private Map<String, TagBean> tagBeanMap;
    private Map<Integer, Set<String>> levelTagMap;

    private Recorder() {
        originData = new ArrayList<>();
        tagBeanMap = new HashMap<>();
        levelTagMap = new HashMap<>();
    }

    public Set<String> getLogTagList(Set<Integer> levels) {
        Set<String> tags = new HashSet<>();
        for (Integer level : levels) {
            Set<String> tagSet = levelTagMap.get(level);
            if (tagSet != null && !tagSet.isEmpty()) {
                tags.addAll(tagSet);
            }
        }
        return tags;
    }

    public List<LogBean> getLogListByLevels(Set<Integer> levels) {
        List<LogBean> logBeans = new ArrayList<>();
        for (LogBean bean : originData) {
            if (levels.contains(bean.getLogLevel())) {
                logBeans.add(bean);
            }
        }
        return logBeans;
    }

    public List<LogBean> getLogList(Set<Integer> levels, Set<String> tags) {
        List<LogBean> logBeans = new ArrayList<>();
        for (String tag : tags) {
            TagBean tagBean = tagBeanMap.get(tag);
            if (tagBean != null) {
                List<LogBean> logBeanList = tagBean.logBeanList;
                if (!logBeanList.isEmpty()) {
                    for (LogBean bean : logBeanList) {
                        if (levels.contains(bean.getLogLevel())) {
                            logBeans.add(bean);
                        }
                    }
                }
            }
        }
        return logBeans;
    }

    public List<LogBean> getAllLogList() {
        return new ArrayList<>(originData);
    }

    /**
     * 添加日志到容器中
     *
     * @param logBean
     */
    public void addLog(LogBean logBean) {
        originData.add(logBean);
        int size = originData.size();
        if (size > MonitoringInitializer.getInstance().getMaxLogCount()) {
            int removeSize = size - MonitoringInitializer.getInstance().getMaxLogCount() + 1;
            List<LogBean> removeLogBeans = originData.subList(0, removeSize);
            for (LogBean item : removeLogBeans) {
                int logLevel = item.getLogLevel();
                String logTag = item.getLogTag();
                // 从相应的标签列表中移除
                TagBean tagBean = tagBeanMap.get(logTag);
                if (tagBean != null) {
                    List<LogBean> logBeanList = tagBean.logBeanList;
                    if (!logBeanList.isEmpty()) {
                        logBeanList.remove(item);
                    }
                    if (logBeanList.isEmpty()) {
                        tagBeanMap.remove(logTag);
                    }
                }
                Set<String> tagSet = levelTagMap.get(logLevel);
                if (tagSet != null) {
                    tagSet.remove(logTag);
                    if (tagSet.isEmpty()) {
                        levelTagMap.remove(logLevel);
                    }
                }
            }
            originData.removeAll(removeLogBeans);
        }

        String logTag = logBean.getLogTag();
        int logLevel = logBean.getLogLevel();
        // 维护tagBeanMap
        TagBean tagBean = tagBeanMap.get(logTag);
        if (tagBean != null) {
            List<LogBean> logBeanList = tagBean.logBeanList;
            logBeanList.add(logBean);
        } else {
            tagBean = new TagBean();
            tagBean.tagName = logTag;
            tagBean.logBeanList = new ArrayList<>();
            tagBean.logBeanList.add(logBean);
            tagBeanMap.put(logTag, tagBean);
        }
        // 维护levelTagMap
        Set<String> tagSet = levelTagMap.get(logLevel);
        if (tagSet == null) {
            tagSet = new HashSet<>();
            levelTagMap.put(logLevel, tagSet);
        }
        tagSet.add(logTag);
        if (MonitoringInitializer.getInstance().getContext() == null) {
            return;
        }
        // 发送广播
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(INTENT_FILTER_LOG_EVENT);
        broadcastIntent.putExtra("data", logBean);
        MonitoringInitializer.getInstance().getContext().sendBroadcast(broadcastIntent);

        // 判断是否有权限
        if (AppOpsManagerUtil.checkDrawOverlays(MonitoringInitializer.getInstance().getContext())) {
            // 构建日志实体，显示在悬浮窗口
            Intent intent = new Intent(MonitoringInitializer.getInstance().getContext(), LogService.class);
            intent.putExtra("data", logBean);
            MonitoringInitializer.getInstance().getContext().startService(intent);
        }
    }

    /**
     * 清空数据
     */
    public void clear() {
        originData.clear();
        tagBeanMap.clear();
        levelTagMap.clear();
    }

}
