package cn.roy.zlib.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import androidx.annotation.RequiresApi;

/**
 * @Description
 * @Author Roy Z
 * @Date 2021/8/8
 * @Version V1.0.0
 */
class MyScrollView extends ScrollView {
    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private boolean mIsWebViewOnBottom;
    private boolean mIsOthersLayoutShow;
    private float mDownY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mDownY;
                if (dy < 0) {// 手指向上滑
                    if (!mIsWebViewOnBottom)
                        return false;// 网页未到底，不拦截事件
                } else {// 手指向下滑
                    if (!mIsOthersLayoutShow)
                        return false;// 底部原生layout完全隐藏，不拦截事件
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setIsWebViewOnBottom(boolean onBottom) {
        this.mIsWebViewOnBottom = onBottom;
    }

    public void setIsOthersLayoutShow(boolean isOthersLayoutShow) {
        this.mIsOthersLayoutShow = isOthersLayoutShow;
    }

}
