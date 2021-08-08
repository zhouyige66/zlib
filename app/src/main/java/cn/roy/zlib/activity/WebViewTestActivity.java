package cn.roy.zlib.activity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.roy.zlib.R;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2021/08/08
 * @Version: v1.0
 */
public class WebViewTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_test);

        WebView webView = findViewById(R.id.wv);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.baidu.com");
    }
}
