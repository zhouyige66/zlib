package cn.roy.zlib.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
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
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.baidu.com");
        webView.setOnSlideListener(new MyWebView.OnSlideListener() {
            @Override
            public void onSlideToTop() {
            }

            @Override
            public void onSlideToBottom() {
            }
        });

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
