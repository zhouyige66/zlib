package cn.roy.zlib.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2021/08/08
 * @Version: v1.0
 */
public class MyWebView extends WebView implements NestedScrollingChild3 {
    private static final String TAG = "MyWebView";

    private NestedScrollingChildHelper nestedScrollingChildHelper;
    private int scrollPointerId;
    private int lastTouchY = 0;
    private final int[] mScrollConsumed = new int[2];
    private final int[] mScrollOffset = new int[2];
    private boolean slideUp = false;// 向上滑动
    private boolean slideToTop = true;// 默认已滑到顶部
    private boolean slideToBottom = false;

    public MyWebView(Context context) {
        this(context, null);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent down");
                scrollPointerId = ev.getPointerId(0);
                lastTouchY = (int) (ev.getY() + 0.5f);
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_TOUCH);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                scrollPointerId = ev.getPointerId(ev.getActionIndex());
                lastTouchY = (int) (ev.getY(ev.getActionIndex()) + 0.5f);
                break;
            case MotionEvent.ACTION_MOVE:
                final int index = ev.findPointerIndex(scrollPointerId);
                if (index < 0) {
                    Log.e(TAG, "Error processing scroll; pointer index for id "
                            + scrollPointerId + " not found. Did any MotionEvents get skipped?");
                    return false;
                }
                final int y = (int) (ev.getY(index) + 0.5f);
                int dy = lastTouchY - y;
                slideUp = dy > 0;
                Log.d(TAG, "onTouchEvent move，偏移量：" + dy);
                mScrollConsumed[0] = 0;
                mScrollConsumed[1] = 0;
                if (this.dispatchNestedPreScroll(0, dy, this.mScrollConsumed,
                        this.mScrollOffset, ViewCompat.TYPE_TOUCH)) {
                    dy -= this.mScrollConsumed[1];
                    requestDisallowInterceptTouchEvent(true);
                }
                Log.d(TAG, "onTouchEvent move，剩余偏移量：" + dy);
                if (dy != 0) {
                    if (slideUp) {
                        slideToTop = false;
                    } else {
                        slideToBottom = false;
                    }
                    scrollBy(0, dy);
                }
                lastTouchY = y - mScrollOffset[1];
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                stopNestedScroll(ViewCompat.TYPE_TOUCH);
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        Log.d(TAG, "onOverScrolled,scrollX=" + scrollX + ",scrollY=" + scrollY
                + ",clampedX=" + clampedX + ",clampedY=" + clampedY);
        if (clampedY) {
            if (slideUp) {
                slideToBottom = true;
            } else {
                slideToTop = true;
            }
        } else {
            if (slideUp) {
                slideToBottom = false;
            } else {
                slideToTop = false;
            }
        }
        Log.d(TAG, "onOverScrolled，是否向上滑：" + slideUp + "，WebView滑动到顶部：" + slideToTop
                + ",到底部：" + slideToBottom);
    }

    private void init() {
        nestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        nestedScrollingChildHelper.setNestedScrollingEnabled(true);
    }

    public boolean isSlideToTop() {
        return slideToTop;
    }

    public boolean isSlideToBottom() {
        return slideToBottom;
    }

    public void resetSlideFlag() {
        slideToTop = true;
        slideToBottom = false;
    }

    /**********TODO 功能：NestedScrollingChild3相关方法**********/
    @Override
    public void dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                     int dyUnconsumed, @Nullable int[] offsetInWindow, int type,
                                     @NonNull int[] consumed) {
        nestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed,
                dyUnconsumed, offsetInWindow, type, consumed);
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        return nestedScrollingChildHelper.startNestedScroll(axes, type);
    }

    @Override
    public void stopNestedScroll(int type) {
        nestedScrollingChildHelper.stopNestedScroll(type);
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        return nestedScrollingChildHelper.hasNestedScrollingParent(type);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, @Nullable int[] offsetInWindow,
                                        int type) {
        return nestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow, type);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed,
                                           @Nullable int[] offsetInWindow, int type) {
        return nestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow,
                type);
    }

}
