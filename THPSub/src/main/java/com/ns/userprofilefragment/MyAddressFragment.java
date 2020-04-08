package com.ns.userprofilefragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.netoperation.model.UserProfile;
import com.netoperation.net.ApiManager;
import com.ns.clevertap.CleverTapUtil;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.thpremium.R;
import com.ns.utils.FragmentUtil;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.text.CustomTextView;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MyAddressFragment extends BaseFragmentTHP {

    private String mFrom;
    private CustomTextView address_TV;
    private CustomTextView addNewAddress_Txt;

    private UserProfile mUserProfile;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_my_address;
    }

    public static MyAddressFragment getInstance(String from) {
        MyAddressFragment fragment = new MyAddressFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            mFrom = getArguments().getString("from");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        address_TV = view.findViewById(R.id.address_TV);
        addNewAddress_Txt = view.findViewById(R.id.addNewAddress_Txt);

        // Back button click listener
        view.findViewById(R.id.backBtn).setOnClickListener(v->{
            FragmentUtil.clearSingleBackStack((AppCompatActivity)getActivity());
        });

        addNewAddress_Txt.setOnClickListener(v->{
            AddAddressFragment fragment = AddAddressFragment.getInstance("");
            FragmentUtil.pushFragmentAnim((AppCompatActivity)getActivity(), R.id.parentLayout, fragment,
                    FragmentUtil.FRAGMENT_NO_ANIMATION, false);
            THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Add Edit Address clicked", MyAddressFragment.class.getSimpleName());
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserProfileData();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "My Address Screen", MyAddressFragment.class.getSimpleName());

    }

    /**
     * // Loads User Profile Data from local Database
     */
    private void loadUserProfileData() {
        mDisposable.add(ApiManager.getUserProfile(getActivity())
                .observeOn(AndroidSchedulers.mainThread())
                .map(userProfile -> {
                    if (userProfile == null) {
                        return "";
                    }

                    mUserProfile = userProfile;

                    if(mUserProfile.getAddress_pincode().trim().length() == 0
                    && mUserProfile.getAddress_house_no().trim().length() == 0
                    && mUserProfile.getAddress_street().trim().length() == 0
                    && mUserProfile.getAddress_landmark().trim().length() == 0
                    && mUserProfile.getAddress_city().trim().length() == 0
                    && mUserProfile.getAddress_state().trim().length() == 0
                    && mUserProfile.getAddress_default_option().trim().length() == 0
                    ) {
                        addNewAddress_Txt.setText("Add New Address");
                        addNewAddress_Txt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add, 0, 0 , 0);
                        //CleverTap
                        HashMap<String,Object> map = new HashMap<>();
                        map.put(THPConstants.CT_KEY_My_address,"No");
                        CleverTapUtil.cleverTapEvent(getActivity(),THPConstants.CT_EVENT_PROFILE,map);
                    } else {
                        addNewAddress_Txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0 , 0);
                        addNewAddress_Txt.setText("Edit Address");
                        addNewAddress_Txt.setPadding(getResources().getDimensionPixelOffset(R.dimen.address_action_padding_start), 0, 0, 0);
                        address_TV.setText(mUserProfile.getAddressFormatted());
                        ResUtil.doStyleSpanForFirstString(mUserProfile.getAddress_default_option() + "\n\n",
                                address_TV.getText().toString(), address_TV);
                    }

                    return "";
                })
                .subscribe(v -> {
                        },
                        t -> {
                            Log.i("", "" + t);
                        }));
    }


}
