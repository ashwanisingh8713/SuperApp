package com.ns.callbacks;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.core.view.GestureDetectorCompat;

import com.netoperation.config.model.TabsBean;

public class TabClickListener  implements GestureDetector.OnGestureListener {

    public interface OnTabClickListener {
        void onTabClick(int tabIndex, TabsBean tabsBean);
    }

    private OnTabClickListener mTabClickListener;

    public void setTabClickListener(OnTabClickListener tabClickListener) {
        this.mTabClickListener = tabClickListener;
    }

    private int tabIndex;
    private TabsBean tabsBean;

    public TabClickListener(int tabIndex, TabsBean tabsBean, OnTabClickListener tabClickListener){
        this.tabsBean = tabsBean;
        this.tabIndex = tabIndex;
        mTabClickListener = tabClickListener;
    }


    @Override
    public boolean onDown(MotionEvent e) {
        Log.i("Gesture", "onDown");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.i("Gesture", "onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.i("Gesture", "onSingleTapUp");
        if(mTabClickListener != null) {
            mTabClickListener.onTabClick(tabIndex, tabsBean);
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.i("Gesture", "onScroll");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.i("Gesture", "onLongPress");

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.i("Gesture", "onFling");
        return false;
    }
}
