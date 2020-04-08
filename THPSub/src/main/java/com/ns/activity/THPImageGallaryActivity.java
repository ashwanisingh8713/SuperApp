package com.ns.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.ns.adapter.ImagePagerAdapter;
import com.ns.model.ImageGallaryUrl;
import com.ns.thpremium.R;
import com.ns.utils.THPFirebaseAnalytics;

import java.util.ArrayList;


public class THPImageGallaryActivity extends BaseAcitivityTHP {
    private final String TAG = "THPImageGallaryActivity";
    private ViewPager mImageViewPager;
    private TextView mErrorText;
    private ProgressBar mProgressBar;
    private LinearLayout mProgressContainer;
    private ArrayList<ImageGallaryUrl> mImageUrlList;
    private TextView mSelectionIndicatorView;
    private int mSelectedPosition;


    @Override
    public int layoutRes() {
        return R.layout.thp_activity_image_gallary;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrlList = getIntent().getParcelableArrayListExtra("ImageUrl");
        mSelectedPosition = getIntent().getIntExtra("selectedPosition", 0);
        mImageViewPager = findViewById(R.id.imagePager);
        mProgressBar = findViewById(R.id.section_progress);
        mProgressContainer = findViewById(R.id.progress_container);
        mErrorText = findViewById(R.id.error_text);
        mSelectionIndicatorView = findViewById(R.id.selection_indicator);

        if (mImageUrlList != null && mImageUrlList.size() > 0) {
            mProgressContainer.setVisibility(View.GONE);
            mImageViewPager.setAdapter(new ImagePagerAdapter(this, mImageUrlList));
            mImageViewPager.setCurrentItem(mSelectedPosition);
            if (mImageUrlList.size() == 1) {
                mSelectionIndicatorView.setVisibility(View.GONE);
            } else {
                mSelectionIndicatorView.setText((mSelectedPosition+1)+"/" + mImageUrlList.size());
            }
        } else {
            mProgressBar.setVisibility(View.GONE);
            mErrorText.setVisibility(View.VISIBLE);
        }
        mImageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int imageOriginalPosition = position + 1;
                mSelectionIndicatorView.setText("" + imageOriginalPosition + "/" + mImageUrlList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // UnComment below if image count is required to show
        // Comment below if image count is not required to show
        if (mImageUrlList != null && mImageUrlList.size() > 1) {
            mSelectionIndicatorView.setVisibility(View.VISIBLE);
        } else {
            mSelectionIndicatorView.setVisibility(View.GONE);
        }

        findViewById(R.id.closeImgThp).setOnClickListener(v->{
            finish();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(this, "THPImageGallaryActivity Screen", THPImageGallaryActivity.class.getSimpleName());
    }
}
