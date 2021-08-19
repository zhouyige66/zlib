package cn.roy.zlib.activity;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import cn.roy.zlib.R;
import cn.roy.zlib.view.MyNestedScrollView;
import cn.roy.zlib.view.MyWebView;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2021/08/08
 * @Version: v1.0
 */
public class WebViewTestActivity extends AppCompatActivity {
    private static final String TAG = "WebViewTestActivity";

    private ViewGroup webViewContainer;
    private MyWebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_test);

        webViewContainer = findViewById(R.id.webViewContainer);
        webView = findViewById(R.id.wv);
        MyNestedScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.setNestedScrollingEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                Log.d(TAG, "2.加载url:" + url);
                webView.resetSlideFlag();
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, "1.加载url:" + url);
                webView.resetSlideFlag();
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.baidu.com");

        // 修改宽高
        DisplayMetrics metric = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindowManager().getDefaultDisplay().getRealMetrics(metric);
        } else {
            getWindowManager().getDefaultDisplay().getMetrics(metric);
        }
        int width = metric.widthPixels; // 宽度（PX）
        int height = metric.heightPixels; // 高度（PX）
        ViewGroup.LayoutParams layoutParams = webView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        webViewContainer.setLayoutParams(layoutParams);
        webViewContainer.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
    }

}
