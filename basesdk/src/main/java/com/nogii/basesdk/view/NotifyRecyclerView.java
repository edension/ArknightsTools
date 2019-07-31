package com.nogii.basesdk.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class NotifyRecyclerView extends RecyclerView {

    private OnNotifyRecyclerCallBack onNotifyRecyclerCallBack;

    public NotifyRecyclerView(Context context) {
        super(context);
    }

    public NotifyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NotifyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void removeViewAt(int index) {
        super.removeViewAt(index);
    }

    public OnNotifyRecyclerCallBack getOnNotifyRecyclerCallBack() {
        return onNotifyRecyclerCallBack;
    }

    public void setOnNotifyRecyclerCallBack(OnNotifyRecyclerCallBack onNotifyRecyclerCallBack) {
        this.onNotifyRecyclerCallBack = onNotifyRecyclerCallBack;
    }

    public interface OnNotifyRecyclerCallBack {
        void onNotifyRemoveDetachedView(View child);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        //返回false，则把事件交给子控件的onInterceptTouchEvent()处理
        return false;
    }

    @Override
        public boolean onTouchEvent(MotionEvent e) {
        //返回true,则后续事件可以继续传递给该View的onTouchEvent()处理
        return false;
    }
}
