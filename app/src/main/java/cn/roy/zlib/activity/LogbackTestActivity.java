package cn.roy.zlib.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ch.qos.logback.classic.Level;
import cn.roy.module.permission_ext.RequestPermission;
import cn.roy.zlib.R;
import cn.roy.zlib.log.AndroidStorageUtil;
import cn.roy.zlib.log.FileAppenderProperty;
import cn.roy.zlib.log.LogConfigBuilder;
import cn.roy.zlib.log.LogUtil;

public class LogbackTestActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logback_test);

        setTitle("Logback Test");
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);
        findViewById(R.id.button7).setOnClickListener(this);

        // 配置默认
        new LogConfigBuilder(this).setRootLevel(Level.DEBUG).buildDefault();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                singleModel();
                break;
            case R.id.button2:
                multipleModel();
                break;
            case R.id.button3:
                LogUtil.v("点击了测试1");
                break;
            case R.id.button4:
                LogUtil.d("点击了测试2");
                break;
            case R.id.button5:
                LogUtil.i("点击了测试3");
                break;
            case R.id.button6:
                LogUtil.w("点击了测试4");
                break;
            case R.id.button7:
                LogUtil.e("点击了测试5");
                break;
            default:
                break;
        }
    }

    private void singleModel() {
        String storagePath = Environment.getDataDirectory().getAbsolutePath();
        boolean granted = AndroidStorageUtil.isStoragePermissionGranted(this);
        if (AndroidStorageUtil.isExternalStorageAvailable() && granted) {
            storagePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            storagePath = storagePath.concat(File.separator).concat(this.getPackageName());
        }
        String logFileDir = storagePath.concat(File.separator).concat("log");
        String logFileName = logFileDir + File.separator + "log.txt";
        String fileNamePattern = logFileDir + File.separator + "log_%d{yyyy-MM-dd}@%i.txt";
        FileAppenderProperty prop = new FileAppenderProperty.Builder(Level.INFO)
                .setTotalFileSize(1024 * 30)
                .setSingleFileSize(1024 * 10)
                .setMaxHistory(2)
                .setLogFilePath(logFileName)
                .setLogFileNamePattern(fileNamePattern)
                .build();
        new LogConfigBuilder(this)
                .setRootLevel(Level.DEBUG)
                .setLogcatAppenderProp(Level.INFO, FileAppenderProperty.PATTERN_DEFAULT)
                .buildForSingleFileModel(prop);
    }

    private void multipleModel() {
        String storagePath = Environment.getDataDirectory().getAbsolutePath();
        boolean granted = AndroidStorageUtil.isStoragePermissionGranted(this);
        if (AndroidStorageUtil.isExternalStorageAvailable() && granted) {
            storagePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            storagePath = storagePath.concat(File.separator).concat(this.getPackageName());
        }
        // 日志文件夹
        String logFilePath = storagePath.concat(File.separator).concat("log");
        String logFileName = logFilePath + File.separator + "%s.txt";
        String fileNamePattern = logFilePath + File.separator + "%s_%d{yyyy-MM-dd}@%i.txt";
        FileAppenderProperty debugProp = create(Level.DEBUG, logFileName, fileNamePattern);
        FileAppenderProperty infoProp = create(Level.INFO, logFileName, fileNamePattern);
        FileAppenderProperty warnProp = create(Level.WARN, logFileName, fileNamePattern);
        FileAppenderProperty errorProp = create(Level.ERROR, logFileName, fileNamePattern);
        List<FileAppenderProperty> list = new ArrayList<>();
        list.add(debugProp);
        list.add(infoProp);
        list.add(warnProp);
        list.add(errorProp);
        new LogConfigBuilder(this)
                .setRootLevel(Level.DEBUG)
                .setLogcatAppenderProp(Level.INFO, FileAppenderProperty.PATTERN_DEFAULT)
                .buildForMultipleFileModel(list);
    }

    private FileAppenderProperty create(Level level, String logFileName, String fileNamePattern) {
        String logNamePrefix = level.levelStr.toLowerCase();
        String logFilePath = String.format(logFileName, logNamePrefix);
        String rollingFileNamePattern = fileNamePattern.replace("%s", logNamePrefix);

        // 读取手机存储
        long internalTotalSize = AndroidStorageUtil.getInternalTotalSize();
        long sdCardTotalSize = AndroidStorageUtil.getSDCardTotalSize();
        long max = Math.max(internalTotalSize, sdCardTotalSize);
        // 存储文件总大小为存储的1/10;
        long totalFileSize = max / 20;
        // 单个文件最大10M
        long singleFileSize = 1024 * 1024 * 10;
        // 默认保存最大天数为7
        int maxHistory = 7;
        FileAppenderProperty prop = new FileAppenderProperty.Builder(level)
                .setLogFilePath(logFilePath)
                .setLogFileNamePattern(rollingFileNamePattern)
                .setSingleFileSize(singleFileSize)
                .setTotalFileSize(totalFileSize)
                .setMaxHistory(maxHistory)
                .build();

        return prop;
    }

    @RequestPermission(permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE},
            autoApply = true, applyPermissionTip = "应用需要存储权限，请授予存储权限")
    public void storage(String path) {

    }
}
