package com.ns.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.ns.thpremium.R;


public class UserJourneyAdapter extends PagerAdapter {

    private String [] titles = {"Support quality journalism", "3 times a day Briefing", "Ad free experience", "News you can trust"};
    private String [] subTitles = {"", "Our Editors gives you all the latest updates and developments of the news thrice a day 8AM, 2PM & 8PM.",
            "Enjoy reading our article without intrusion from advertisements", "Coming soon here..."};
    private int [] drawables = {R.drawable.uj_1, R.drawable.ic_uj_2, R.drawable.ic_uj_3, R.drawable.ic_uj_4 };

    private Context mContext;

    public UserJourneyAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.pageritem_userjourney, container, false);
        ImageView imageView = view.findViewById(R.id.imgView);
        TextView journeyTitle = view.findViewById(R.id.journeyTitle);
        TextView journeySubTitle = view.findViewById(R.id.journeySubTitle);

        journeyTitle.setText(titles[position]);
        journeySubTitle.setText(subTitles[position]);
        imageView.setImageResource(drawables[position]);

        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
