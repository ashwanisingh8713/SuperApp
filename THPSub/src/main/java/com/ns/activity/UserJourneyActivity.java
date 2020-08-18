package com.ns.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.ns.adapter.UserJourneyAdapter;
import com.ns.thpremium.R;
import com.ns.utils.IntentUtil;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.view.text.CustomTextView;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

public class UserJourneyActivity extends BaseAcitivityTHP {


    private ViewPager mViewPager;
    private SpringDotsIndicator mDotsIndicator;
    private SpringDotsIndicator mDotsIndicator_;
    private CustomTextView journeyNextBtn;


    @Override
    public int layoutRes() {
        return R.layout.activity_user_journey;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager = findViewById(R.id.viewPager);
        mDotsIndicator = findViewById(R.id.spring_dots_indicator);
        mDotsIndicator_ = findViewById(R.id.spring_dots_indicator_);
        journeyNextBtn = findViewById(R.id.journeyNextBtn);

        mViewPager.setAdapter(new UserJourneyAdapter(this));
        mDotsIndicator.setViewPager(mViewPager);
        mDotsIndicator_.setViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 3) {
                    journeyNextBtn.setText("Complete");
                    journeyNextBtn.setBackgroundColor(ResUtil.getColor(getResources(), R.color.uj_complete));
                } else {
                    //journeyNextBtn.setText("Next :: "+getString(R.string.type_device));
                    journeyNextBtn.setText("Next");
                    journeyNextBtn.setTextColor(ResUtil.getColor(getResources(), R.color.uj_next));
                    journeyNextBtn.setBackground(null);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        journeyNextBtn.setOnClickListener(v->{
            if(mViewPager.getCurrentItem() == mViewPager.getAdapter().getCount()-1) {
                IntentUtil.openSubscriptionActivity(this, THPConstants.FROM_USER_JOURNEY);
            } else {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
            }
        });


    }


}
