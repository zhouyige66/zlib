package cn.roy.zlib.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Trace;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.roy.zlib.R;
import cn.roy.zlib.log.LogUtil;
import cn.roy.zlib.monitor.ExceptionInfoLogger;
import cn.roy.zlib.monitor.Monitor;
import cn.roy.zlib.permission.PermissionGrantActivity;
import cn.roy.zlib.permission.PermissionUtil;
import cn.roy.zlib.tool.MonitoringToolSDK;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private boolean hasPermission = false;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("首页");
        button = findViewById(R.id.button1);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);
        findViewById(R.id.button7).setOnClickListener(this);

        new Monitor(this)
                .setBlockMonitorEnable(true)
                .setBlockMonitorTimeout(2000)
                .setCrashMonitorEnable(true)
                .setCrashLogAutoSave(true)
                .setExceptionInfoLogger(new ExceptionInfoLogger() {
                    @Override
                    public void print(String exceptionInfo) {
                        LogUtil.e(exceptionInfo);
                    }
                })
                .init();
        hasPermission = PermissionUtil.hasPermissions(this, permissions);
        if (hasPermission) {
            button.setText("已授权");
        }
        MonitoringToolSDK.init(new MonitoringToolSDK.Options(this).setMaxLogCount(50));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PermissionGrantActivity.CODE_PERMISSION_GRANT_REQUEST) {
            if (resultCode == PermissionGrantActivity.PERMISSIONS_GRANTED) {
                button.setText("已授权");
            } else {
                Toast.makeText(this, "读写权限未被授予，请重试", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                requestPermission();
                break;
            case R.id.button2:
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.button3:
                int i = 0;
                int k = 100 / i;
                Log.d("kk20", "计算结果：" + k);
                break;
            case R.id.button4:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    Trace.beginSection("onClick");
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    Trace.endSection();
                }
                break;
            case R.id.button5:
                Debug.startMethodTracing();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Debug.stopMethodTracing();
                break;
            case R.id.button6:
                startActivity(new Intent(this, LogbackTestActivity.class));
                break;
            case R.id.button7:
                startActivity(new Intent(this, HttpTestActivity.class));
                break;
            default:
                break;
        }
    }

    private void requestPermission() {
        PermissionGrantActivity.jump2PermissionGrantActivity(this, permissions);
    }

}
