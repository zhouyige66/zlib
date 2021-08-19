package cn.roy.zlib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

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
            int locationY = mWebViewLocation[1];
            Log.d(TAG, "WebView距离顶点：" + locationY);
            Log.d(TAG, "滑动距离：" + dy);
            if (dy > 0) {// 向上滑动
                if (locationY > 0) {// 外层视图消耗滑动
                    Log.d(TAG, "未到顶点，消耗滑动距离");
                    int moveY = Math.min(locationY, dy);
                    scrollBy(0, moveY);
                    consumed[1] = moveY;
                } else {
                    boolean slideToBottom = myWebView.isSlideToBottom();
                    Log.d(TAG, "当前网页滑动到底部：" + slideToBottom);
                    if (slideToBottom) {
                        scrollBy(0, dy);
                        consumed[1] = dy;
                    }
                }
            } else {
                if (locationY < 0) {
                    int moveY = Math.max(locationY, dy);
                    Log.d(TAG, "未到底点，消耗滑动距离");
                    scrollBy(0, moveY);
                    consumed[1] = moveY;
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
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        Log.d(TAG, "onNestedPreFling");
        return super.onNestedPreFling(target, velocityX, velocityY);
    }
}
