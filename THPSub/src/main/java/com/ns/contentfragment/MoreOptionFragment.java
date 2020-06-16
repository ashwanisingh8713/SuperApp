package com.ns.contentfragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ns.thpremium.R;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.utils.IntentUtil;
import com.ns.utils.THPFirebaseAnalytics;

public class MoreOptionFragment extends BaseFragmentTHP {

    public static MoreOptionFragment getInstance(String userId) {
        MoreOptionFragment fragment = new MoreOptionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_moreoption;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mUserId = getArguments().getString("userId");
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.suggested_Txt).setOnClickListener(v->{
//            IntentUtil.openBookmarkActivity(getActivity(), THPConstants.FROM_BookmarkListing, mUserId);
            Intent intent = new Intent();
            intent.putExtra("userId", mUserId);
            intent.setAction("com.ash.thp.bookmarks.opening");
            startActivity(intent);

        });


        view.findViewById(R.id.personalise_Txt).setOnClickListener(v->{
            IntentUtil.openPersonaliseActivity(getActivity(), "MoreOptions");
        });





    }


    @Override
    public void onResume() {
        super.onResume();
         //Firebase Analytics
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "MoreOption Fragment Screen", MoreOptionFragment.class.getSimpleName());
    }
}
