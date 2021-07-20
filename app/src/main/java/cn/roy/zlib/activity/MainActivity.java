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

import java.io.File;

import cn.roy.zlib.R;
import cn.roy.zlib.log.LogUtil;
import cn.roy.zlib.permission.PermissionGrantActivity;
import cn.roy.zlib.permission.PermissionUtil;

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

        hasPermission = PermissionUtil.hasPermissions(this, permissions);
        if (hasPermission) {
            button.setText("读写权限：已授权");
        }
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
                hasPermission = true;
                button.setText("读写权限：已授权");
            } else {
                Toast.makeText(this, "读写权限未被授予，请重试", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                LogUtil.d("点击了获取授权按钮");
                if (!hasPermission) {
                    PermissionGrantActivity.jump2PermissionGrantActivity(this, permissions);
                } else {
                    LogUtil.d("已经获取授权");
                }
                break;
            case R.id.button2:
                LogUtil.d("卡顿测试");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.button3:
                LogUtil.d("崩溃测试");
                int i = 0;
                int k = 100 / i;
                LogUtil.d("k==" + k);
                break;
            case R.id.button4:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    Trace.beginSection("Roy");
                }
                try {
                    Thread.sleep(1000);
                    ((Button) v).setText("Roy");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    Trace.endSection();
                }
                break;
            case R.id.button5:
                String path = getExternalFilesDir(null).getAbsolutePath()
                        + File.separator + "trace"
                        + File.separator + "method_trace.trace";
                File file = new File(path);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                Debug.startMethodTracing(path, 1024 * 1024 * 100);
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

}
