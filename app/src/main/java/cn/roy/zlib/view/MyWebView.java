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
    private int[] position = new int[2];
    private float lastX = 0, lastY = 0;

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

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        requestDisallowInterceptTouchEvent(true);

        getLocationOnScreen(position);
        Log.d("MyWebView", "位置：" + position[0] + "/" + position[1]);
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

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        Log.d("MyWebView", "scrollX=" + scrollX + ",scrollY=" + scrollY
                + ",clampedX=" + clampedX + ",clampedY" + clampedY);
    }

    private boolean canScroll() {
        return getContentHeight() * getScale() == (getHeight() + getScrollY());
    }

}
