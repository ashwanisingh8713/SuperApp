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
import com.ns.loginfragment.SignInFragment;
import com.ns.loginfragment.SignUpFragment;
import com.ns.thpremium.R;

public class SignInAndUpPagerAdapter extends FragmentStatePagerAdapter {

    String mFrom;
    private boolean mIsDayTheme;

    public SignInAndUpPagerAdapter(FragmentManager fm, String from, boolean isDayTheme) {
        super(fm);
        mFrom = from;
        mIsDayTheme = isDayTheme;
    }

    @Override
    public Fragment getItem(int i) {
        if(i==0) {
            return SignUpFragment.getInstance(mFrom);
        }
        else {
            return SignInFragment.getInstance(mFrom);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0) {
            return "Sign Up";
        }
        else {
            return "Sign In";
        }
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

    public View getTabView(int position, Context context, boolean isSelected) {

        String tilte = "";

        if(position == 0) {
            tilte = "Sign Up";
        }
        else {
            tilte = "Sign In";
        }

        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab_signin_up, null);
        TextView tv = v.findViewById(R.id.textView);
        tv.setText(tilte);
        return v;
    }

    public void SetOnSelectView(Context context, TabLayout tabLayout, int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View selected = tab.getCustomView();
        TextView iv_text = selected.findViewById(R.id.textView);
        if(mIsDayTheme) {
            iv_text.setTextColor(context.getResources().getColor(R.color.boldBlackColor));
        } else {
            iv_text.setTextColor(context.getResources().getColor(R.color.dark_color_static_text));
        }
    }

    public void SetUnSelectView(Context context, TabLayout tabLayout,int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View selected = tab.getCustomView();
        TextView iv_text = selected.findViewById(R.id.textView);
        iv_text.setTextColor(context.getResources().getColor(R.color.greyColor_1));
    }
}
