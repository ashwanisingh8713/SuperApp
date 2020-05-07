package com.ns.contentfragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.netoperation.net.ApiManager;
import com.ns.thpremium.R;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.utils.FragmentUtil;
import com.ns.utils.ResUtil;
import com.ns.view.text.CustomTextView;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class EditionOptionFragment extends BaseFragmentTHP {

    public static EditionOptionFragment getInstance() {
        EditionOptionFragment fragment = new EditionOptionFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_editionoption;
    }


    private CustomTextView allEditions_Txt;
    private CustomTextView morningEditions_Txt;
    private CustomTextView noonEditions_Txt;
    private CustomTextView eveningEditions_Txt;

    private String allEdition = "All Editions";
    private String morningEdition = "Morning Editions";
    private String noonEdition = "Noon Editions";
    private String eveningEdition = "Evening Editions";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout shadowLayout = view.findViewById(R.id.shadowLayout);

        if(sIsDayTheme) {
            shadowLayout.setBackground(ResUtil.getBackgroundDrawable(getResources(), R.drawable.shadow_white_r12_s6_wh200_ltr));
        } else {
            shadowLayout.setBackground(ResUtil.getBackgroundDrawable(getResources(), R.drawable.shadow_dark_r12_s6_wh200_ltr));
        }

        // To block the touch or click
        view.findViewById(R.id.moreTabLayout).setOnTouchListener((v,event)->{
            FragmentUtil.clearSingleBackStack((AppCompatActivity) getActivity());
            return true;
        });

        allEditions_Txt = view.findViewById(R.id.allEditions_Txt);
        morningEditions_Txt = view.findViewById(R.id.morningEditions_Txt);
        noonEditions_Txt = view.findViewById(R.id.noonEditions_Txt);
        eveningEditions_Txt = view.findViewById(R.id.eveningEditions_Txt);

        mDisposable.add(ApiManager.getBreifingTimeFromDB(getActivity())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(map->{
                    String morningTime = map.get("morningTime");
                    String noonTime = map.get("noonTime");
                    String eveningTime = map.get("eveningTime");

                    String morningEnable = map.get("morningEnable");
                    String noonEnable = map.get("noonEnable");
                    String eveningEnable = map.get("eveningEnable");


                    if(morningEnable == null) {
                        morningEditions_Txt.setTextColor(ResUtil.getColor(getResources(), R.color.timeColor));
                        morningEditions_Txt.setEnabled(false);
                    } else {
                        morningTime = "";
                    }

                    if(noonEnable == null) {
                        noonEditions_Txt.setTextColor(ResUtil.getColor(getResources(), R.color.timeColor));
                        noonEditions_Txt.setEnabled(false);
                    } else {
                        noonTime = "";
                    }

                    if(eveningEnable == null) {
                        eveningEditions_Txt.setTextColor(ResUtil.getColor(getResources(), R.color.timeColor));
                        eveningEditions_Txt.setEnabled(false);
                    } else {
                        eveningTime = "";
                    }
                    morningEditions_Txt.setText(morningEdition+" "+morningTime);
                    noonEditions_Txt.setText(noonEdition+" "+noonTime);
                    eveningEditions_Txt.setText(eveningEdition+" "+eveningTime);

                }, throwable -> {

                }));

        allEditions_Txt.setOnClickListener(v->{
            if(mOnEditionOptionClickListener != null) {
                mOnEditionOptionClickListener.onEditionOptionClickListener(allEdition);
            }
        });


        morningEditions_Txt.setOnClickListener(v->{
            if(mOnEditionOptionClickListener != null) {
                mOnEditionOptionClickListener.onEditionOptionClickListener(morningEdition);
            }
        });


        noonEditions_Txt.setOnClickListener(v->{
            if(mOnEditionOptionClickListener != null) {
                mOnEditionOptionClickListener.onEditionOptionClickListener(noonEdition);
            }
        });


        eveningEditions_Txt.setOnClickListener(v->{
            if(mOnEditionOptionClickListener != null) {
                mOnEditionOptionClickListener.onEditionOptionClickListener(eveningEdition);
            }
        });

    }

    private OnEditionOptionClickListener mOnEditionOptionClickListener;

    public void setOnEditionOptionClickListener(OnEditionOptionClickListener onEditionOptionClickListener) {
        mOnEditionOptionClickListener = onEditionOptionClickListener;
    }


    public interface OnEditionOptionClickListener {
        void onEditionOptionClickListener(String value);
    }
}
