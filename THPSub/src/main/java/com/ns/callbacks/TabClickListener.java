package com.ns.callbacks;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.core.view.GestureDetectorCompat;

public class TabClickListener  implements GestureDetector.OnGestureListener {

    public interface OnTabClickListener {
        void onTabClick(int tabIndex, String tabGroup);
    }

    private OnTabClickListener mTabClickListener;

    public void setTabClickListener(OnTabClickListener tabClickListener) {
        this.mTabClickListener = tabClickListener;
    }

    private int tabIndex;
    private String tabGroup;

    public TabClickListener(int tabIndex, String tabGroup, OnTabClickListener tabClickListener){
        this.tabGroup = tabGroup;
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
            mTabClickListener.onTabClick(tabIndex, tabGroup);
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
