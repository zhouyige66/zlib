package cn.roy.zlib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

/**
 * @Description
 * @Author Roy Z
 * @Date 2021/8/8
 * @Version V1.0.0
 */
public class MyNestedScrollView extends NestedScrollView {
    private static final String TAG = "MyNestedScrollView";

    private final int[] mWebViewLocation = new int[2];

    public MyNestedScrollView(Context context) {
        super(context);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**********TODO 功能：以下是嵌套滑动相关方法**********/

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        Log.d(TAG, "onStartNestedScroll");
        return super.onStartNestedScroll(child, target, axes, type);
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        Log.d(TAG, "onNestedScrollAccepted");
        super.onNestedScrollAccepted(child, target, axes, type);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        Log.d(TAG, "onNestedPreScroll");
        // 判断是否消耗滑动距离
        if (target instanceof MyWebView) {
            final MyWebView myWebView = (MyWebView) target;
            target.getLocationOnScreen(mWebViewLocation);
            if (dy > 0) {// 向上滑动
                if (mWebViewLocation[1] > 0) {// 外层视图消耗滑动
                    Log.d(TAG, "未到顶点，消耗滑动距离");
                    scrollBy(0, dy);
                    consumed[1] = dy;
                } else {
                    boolean slideToBottom = myWebView.isSlideToBottom();
                    Log.d(TAG, "当前网页滑动到底部：" + slideToBottom);
                    if (slideToBottom) {
                        scrollBy(0, dy);
                        consumed[1] = dy;
                    }
                }
            } else {
                if (mWebViewLocation[1] < 0) {
                    Log.d(TAG, "未到底点，消耗滑动距离");
                    scrollBy(0, dy);
                    consumed[1] = dy;
                } else {
                    boolean slideToTop = myWebView.isSlideToTop();
                    Log.d(TAG, "当前网页滑动到顶部：" + slideToTop);
                    if (slideToTop) {
                        scrollBy(0, dy);
                        consumed[1] = dy;
                    }
                }
            }
        }
        super.onNestedPreScroll(target, dx, dy, consumed, type);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
    }

}
