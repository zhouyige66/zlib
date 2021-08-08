package cn.roy.zlib.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2021/08/08
 * @Version: v1.0
 */
public class MyWebView extends WebView {
    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private int[] postion = new int[2];
    private float lastX = 0, lastY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        requestDisallowInterceptTouchEvent(true);

        getLocationOnScreen(postion);
        Log.d("MyWebView", "位置：" + postion[0] + "/" + postion[1]);
        Log.d("MyWebView", "指针位置：" + ev.getX() + "/" + ev.getY());
        Log.d("MyWebView", "向上滑动：" + canScrollVertically(-1));
        Log.d("MyWebView", "向下滑动：" + canScrollVertically(1));
        Log.d("MyWebView", "getHeight()=" + getHeight());
        Log.d("MyWebView", "getScale()=" + getScale());
        Log.d("MyWebView", "getContentHeight()=" + getContentHeight());
        Log.d("MyWebView", "scale*contentHeight=" + getContentHeight() * getScale());
        Log.d("MyWebView", "getScrollY()=" + getScrollY());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.d("MyWebView", "l=" + l + ",t=" + t + ",oldl=" + oldl + ",oldt" + oldt);
    }

    private boolean canScroll() {
        return getContentHeight() * getScale() == (getHeight() + getScrollY());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();
        int[] location = new int[2];
        getLocationOnScreen(location);
        return super.onInterceptTouchEvent(ev);
    }

}
