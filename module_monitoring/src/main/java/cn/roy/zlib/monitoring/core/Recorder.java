package cn.roy.zlib.monitoring.core;

import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

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

    private LinkedBlockingQueue<LogBean> logBeanLinkedBlockingQueue;
    private List<LogBean> originData;
    private Map<String, TagBean> tagBeanMap;
    private Map<Integer, Set<String>> levelTagMap;
    private Worker mWorker;

    private Recorder() {
        logBeanLinkedBlockingQueue = new LinkedBlockingQueue<>();
        originData = new ArrayList<>();
        tagBeanMap = new HashMap<>();
        levelTagMap = new HashMap<>();
    }

    /**
     * 清空数据
     */
    public void clear() {
        logBeanLinkedBlockingQueue.clear();
        originData.clear();
        tagBeanMap.clear();
        levelTagMap.clear();
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
        logBeanLinkedBlockingQueue.offer(logBean);
        if (mWorker == null) {
            mWorker = new Worker();
            new Thread(mWorker).start();
        }
    }

    private class Worker implements Runnable {
        private boolean run = true;

        public void stop() {
            this.run = false;
        }

        @Override
        public void run() {
            while (run) {
                try {
                    LogBean logBean = logBeanLinkedBlockingQueue.take();// 当无对象的时候，该方法会堵塞
                    originData.add(logBean);
                    int size = originData.size();
                    if (size > MonitoringInitializer.getInstance().getMaxLogCount()) {
                        int removeSize = size - MonitoringInitializer.getInstance().getMaxLogCount() + 1;
                        List<LogBean> removeLogBeans = originData.subList(0, removeSize);
                        originData.removeAll(removeLogBeans);
                    }

                    String tag = logBean.getLogTag();
                    int level = logBean.getLogLevel();
                    // 维护tagBeanMap
                    TagBean tagBean = tagBeanMap.get(tag);
                    if (tagBean == null) {
                        tagBean = new TagBean();
                        tagBean.tagName = tag;
                        tagBean.logBeanList = new ArrayList<>();
                        tagBean.logBeanList.add(logBean);
                        tagBeanMap.put(tag, tagBean);
                    } else {
                        List<LogBean> logBeanList = tagBean.logBeanList;
                        logBeanList.add(logBean);
                    }
                    // 维护levelTagMap
                    Set<String> tagSet = levelTagMap.get(level);
                    if (tagSet == null) {
                        tagSet = new HashSet<>();
                        levelTagMap.put(level, tagSet);
                    }
                    tagSet.add(tag);

                    if (MonitoringInitializer.getInstance().getContext() == null) {
                        return;
                    }
                    // TODO 序列化数据过长处理
                    String logText = logBean.getLogText();
                    int length = logText.length();
                    ArrayList<String> logTexts = new ArrayList<>();
                    if (length > 1024) {
                        size = length / 1024 + 1;
                        for (int i = 0; i < size; i++) {
                            int index = 1024 * i;
                            logTexts.add(logText.substring(index, index + 1024));
                        }
                    } else {
                        logTexts.add(logText);
                    }
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(INTENT_FILTER_LOG_EVENT);
                    broadcastIntent.putExtra("data", logBean);
                    broadcastIntent.putStringArrayListExtra("logTextArray", logTexts);
                    MonitoringInitializer.getInstance().getContext().sendBroadcast(broadcastIntent);

                    // 判断是否有权限
                    if (AppOpsManagerUtil.checkDrawOverlays(MonitoringInitializer.getInstance().getContext())) {
                        // 构建日志实体，显示在悬浮窗口
                        Intent intent = new Intent(MonitoringInitializer.getInstance().getContext(), LogService.class);
                        intent.putExtra("data", logBean);
                        intent.putStringArrayListExtra("logTextArray", logTexts);
                        MonitoringInitializer.getInstance().getContext().startService(intent);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void notifyOther() {

    }

}
