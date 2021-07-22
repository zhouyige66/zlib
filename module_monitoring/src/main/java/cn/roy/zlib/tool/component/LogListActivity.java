package cn.roy.zlib.tool.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.roy.zlib.tool.R;
import cn.roy.zlib.tool.bean.LogBean;
import cn.roy.zlib.tool.core.LogListAdapter;
import cn.roy.zlib.tool.core.Recorder;

/**
 * @Description: 悬浮日志页面
 * @Author: Roy Z
 * @Date: 2021/07/22
 * @Version: v1.0
 */
public class LogListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LogListAdapter logListAdapter;
    private List<LogBean> logBeanList;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                LogBean data = intent.getParcelableExtra("data");
                if (data != null) {
                    logBeanList.add(data);
                    if (logListAdapter != null) {
                        logListAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_list);

        recyclerView = findViewById(R.id.recyclerView);
        findViewById(R.id.tvClean).setOnClickListener(v -> {
            logBeanList.clear();
            logListAdapter.notifyDataSetChanged();
            Recorder.getInstance().clear();
        });

        logBeanList = new ArrayList<>();
        logBeanList.addAll(Recorder.getInstance().getAllLogList());
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(new ColorDrawable(Color.parseColor("#999999")));
        recyclerView.addItemDecoration(dividerItemDecoration);
        logListAdapter = new LogListAdapter(this, logBeanList);
        recyclerView.setAdapter(logListAdapter);

        IntentFilter intentFilter = new IntentFilter(Recorder.INTENT_FILTER_LOG_EVENT);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(broadcastReceiver);
    }
}
