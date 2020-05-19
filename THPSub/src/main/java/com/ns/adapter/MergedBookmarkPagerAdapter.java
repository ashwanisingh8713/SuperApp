package com.ns.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.netoperation.util.NetConstants;
import com.ns.contentfragment.THP_BookmarksFragment;
import com.ns.thpremium.R;

public class MergedBookmarkPagerAdapter extends FragmentStatePagerAdapter {

    private String mUserId;

    private String[] mTabTitle = {"Subscription", "Non Subscription"};
    private boolean mIsDayTheme;

    private String mGroupType;

    public MergedBookmarkPagerAdapter(FragmentManager fm, String userId, boolean isDayTheme, String groupType) {
        super(fm);
        mUserId = userId;
        mIsDayTheme = isDayTheme;
        mGroupType = groupType;
    }

    @Override
    public Fragment getItem(int i) {
        if(mGroupType.equals(NetConstants.BOOKMARK_DEFAULT_PREMIUM_IN_TAB)) {
            if (i == 0) {
                return THP_BookmarksFragment.getInstance(mUserId, NetConstants.GROUP_PREMIUM_BOOKMARK);
            }
            else {
                return THP_BookmarksFragment.getInstance(mUserId, NetConstants.GROUP_DEFAULT_BOOKMARK);
            }
        }
        else { // (mBookmarkViewType == NetConstants.BOOKMARK_DEFAULT_PREMIUM_IN_ONE)
            return THP_BookmarksFragment.getInstance(mUserId, NetConstants.BOOKMARK_IN_ONE);
        }
    }

    @Override
    public int getCount() {
        if(mGroupType.equals(NetConstants.BOOKMARK_DEFAULT_PREMIUM_IN_TAB)) {
            return 2;
        }
        return 1;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitle[position];
    }

    @Override
    public Parcelable saveState() {
        Bundle bundle = (Bundle) super.saveState();
        if (bundle != null) {
            bundle.putParcelableArray("states", null);
            return bundle;
        }
        return super.saveState();
    }

    public View getTabView(int position, Context context) {
        String tilte = mTabTitle[position];
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab_bookmark_merged, null);
        TextView tv = v.findViewById(com.ns.thpremium.R.id.textView);
        tv.setText(tilte);
        return v;
    }

    public void SetOnSelectView(Context context, TabLayout tabLayout, int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View selected = tab.getCustomView();
        TextView iv_text = selected.findViewById(R.id.textView);
        if(mIsDayTheme) {
            iv_text.setTextColor(context.getResources().getColor(com.ns.thpremium.R.color.color_191919_light));
        } else {
            iv_text.setTextColor(context.getResources().getColor(com.ns.thpremium.R.color.color_ededed_dark));
        }
    }

    public void SetUnSelectView(Context context, TabLayout tabLayout,int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View selected = tab.getCustomView();
        TextView iv_text = selected.findViewById(R.id.textView);
        iv_text.setTextColor(context.getResources().getColor(com.ns.thpremium.R.color.color_818181_light));
    }
}
