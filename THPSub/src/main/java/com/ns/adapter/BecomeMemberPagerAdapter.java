package com.ns.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.ns.thpremium.R;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.view.text.CustomTextView;

public class BecomeMemberPagerAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    private Context context;
    private String mFrom;

    public BecomeMemberPagerAdapter(Context context, String from) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        mFrom = from;
    }

    @Override
    public int getCount() {
        if (mFrom != null && mFrom.equalsIgnoreCase(THPConstants.FROM_SubscriptionStep_1_Fragment)) {
            return 1;
        }
        return 6;
    }

    private int[] imgs = {
            R.drawable.bi_1,
            R.drawable.bi_1,
            R.drawable.bi_2,
            R.drawable.bi_4,
            R.drawable.bi_3,
            R.drawable.bi_5};

    private String[] titles = {
            "Briefing",
            "Briefing",
            "My stories",
            "Personalised recommendation",
            "Ad free experience",
            "Faster page loads"};
    private String[] msgs = {
            "Stay on top of the news! We brief you on the latest and most important developments, three times a day",
            "Stay on top of the news! We brief you on the latest and most important developments, three times a day",
            "Top articles from the topics of interest selected by you",
            "Articles specially selected for you by an automated analysis of your reading interests and patterns",
            "Enjoy reading our article without intrusion from advertisements",
            "Move smoothly between articles as our pages load instantly"};


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int contentLayoutResId;

        if (position == 0) {
            contentLayoutResId = R.layout.fragment_introcontent_1;
        } else {
            contentLayoutResId = R.layout.fragment_introcontent_2;
        }

        final View layout = inflater.inflate(contentLayoutResId, container, false);
        container.addView(layout, 0);

        if (mFrom != null && position == 0 && mFrom.equalsIgnoreCase(THPConstants.FROM_SubscriptionStep_1_Fragment)) {
            TextView becomeMember_Txt = layout.findViewById(R.id.becomeMember_Txt);
            TextView benefitsIncludes_Txt = layout.findViewById(R.id.benefitsIncludes_Txt);
            becomeMember_Txt.setText("Subscription Benefits");
            benefitsIncludes_Txt.setText("Subscribe to get these features free for");
            benefitsIncludes_Txt.setTextColor(ResUtil.getColor(context.getResources(), R.color.boldBlackColor));
            benefitsIncludes_Txt.setAlpha(0.6f);
        } else if (position > 0) {
            ImageView text1 = layout.findViewById(R.id.text1);
            CustomTextView text2 = layout.findViewById(R.id.text2);
            CustomTextView text3 = layout.findViewById(R.id.text3);

            text1.setImageResource(imgs[position]);
            text2.setText(titles[position]);
            text3.setText(msgs[position]);

        }

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
