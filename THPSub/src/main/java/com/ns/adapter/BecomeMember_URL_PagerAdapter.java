package com.ns.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.ns.activity.BaseAcitivityTHP;
import com.ns.thpremium.R;
import com.ns.utils.PicassoUtil;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.view.CustomProgressBar;
import com.ns.view.text.CustomTextView;

import java.util.List;

public class BecomeMember_URL_PagerAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    private Context context;
    private List<String> strings;

    public BecomeMember_URL_PagerAdapter(Context context, List<String> strings) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.strings = strings;
    }

    @Override
    public int getCount() {
        return strings.size();
    }



    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        RelativeLayout relativeLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(layoutParams);

        CustomProgressBar progressBar = new CustomProgressBar(context);
        RelativeLayout.LayoutParams progressParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        progressParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        progressBar.setLayoutParams(progressParams);
        relativeLayout.addView(progressBar);

        ImageView imageView = new ImageView(context);
        imageView.setBackground(null);
        RelativeLayout.LayoutParams imgParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(imgParams);
        relativeLayout.addView(imageView);

        PicassoUtil.loadImage(context, imageView, strings.get(position));

        container.addView(relativeLayout, 0);

        return relativeLayout;
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
