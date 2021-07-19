package cn.roy.zlib.activity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import cn.roy.zlib.R;
import cn.roy.zlib.http.CancelableTask;
import cn.roy.zlib.http.RequestCallback;
import cn.roy.zlib.http.RequestExecutor;
import cn.roy.zlib.http.RetrofitFactory;
import cn.roy.zlib.http.metrics.RequestMetrics;
import cn.roy.zlib.http.metrics.RequestMetricsListener;
import cn.roy.zlib.httptest.APITest;
import cn.roy.zlib.httptest.Result;
import cn.roy.zlib.httptest.User;
import cn.roy.zlib.log.LogUtil;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Retrofit;

public class HttpTestActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private APITest apiTest;
    private boolean hasPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_test);

        setTitle("Http Test");
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);

        initHttp();
        RequestMetrics.Companion.getInstance().registerListener(new RequestMetricsListener() {
            @Override
            public void onStatisticsChange() {
                int initCount = RequestMetrics.StatisticCount.INSTANCE.getInitCount();
                int cancelCount = RequestMetrics.StatisticCount.INSTANCE.getCancelCount();
                int successCount = RequestMetrics.StatisticCount.INSTANCE.getSuccessCount();
                int failCount = RequestMetrics.StatisticCount.INSTANCE.getFailCount();
                LogUtil.d("发起请求：" + initCount + "，取消：" + cancelCount + "，成功："
                        + successCount + "，失败：" + failCount);
            }

            @Override
            public void onAnalysisChange(@NotNull String url, long averageTime) {
                LogUtil.d("最近访问请求：" + url + "，该请求平均耗时：" + averageTime + "ms");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                Call<Result<JsonObject>> call = apiTest.getSessionId();
                CancelableTask task = RequestExecutor.execute(call, new RequestCallback<Result<JsonObject>>() {
                    @Override
                    public void success(Result<JsonObject> jsonObjectResult) {
                        LogUtil.d("" + jsonObjectResult.getCode());
                    }

                    @Override
                    public void fail(int code, @NotNull String msg) {
                        LogUtil.d(msg);
                    }
                });
                break;
            case R.id.button2:
                Observable<Result<User>> observable = apiTest.getUser();
                CancelableTask task2 = RequestExecutor.execute(observable, new RequestCallback<Result<User>>() {
                    @Override
                    public void success(Result<User> user) {
                        LogUtil.d("" + user.getCode());
                    }

                    @Override
                    public void fail(int code, @NotNull String msg) {
                        LogUtil.d(msg);
                    }
                });
                break;
            default:
                break;
        }
    }

    private void initHttp() {
//        Retrofit retrofit = RetrofitFactory.create("http://10.0.2.2:9999/");
//        Retrofit retrofit = RetrofitFactory.create("http://192.168.1.8:9999/");
        Retrofit retrofit = RetrofitFactory.create("http://10.185.42.79:9999/");
        apiTest = retrofit.create(APITest.class);
    }

}
