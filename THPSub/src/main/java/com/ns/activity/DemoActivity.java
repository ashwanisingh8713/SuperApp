package com.ns.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.netoperation.net.ApiManager;
import com.ns.thpremium.R;
import com.ns.utils.IntentUtil;
import com.ns.utils.THPConstants;
import com.ns.view.text.CustomTextView;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class DemoActivity extends BaseAcitivityTHP {


    private ImageView premiumLogoBtn;
    private CustomTextView profileBtn;

    @Override
    public int layoutRes() {
        return R.layout.activity_demo;
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        premiumLogoBtn = findViewById(R.id.action_premiumLogoBtn);
        profileBtn = findViewById(R.id.profileBtn);

        premiumLogoBtn.setVisibility(View.GONE);
        profileBtn.setVisibility(View.GONE);

        premiumLogoBtn.setOnClickListener(v -> {
            IntentUtil.openMemberActivity(this, "");
        });

        profileBtn.setOnClickListener(v -> {
            IntentUtil.openContentListingActivity(this, THPConstants.FROM_USER_PROFILE);
        });


        // https://developers.google.com/identity/sms-retriever/request
        SmsRetrieverClient client = SmsRetriever.getClient(this);

        // Starts SmsRetriever, which waits for ONE matching SMS message until timeout
        // (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
        // action SmsRetriever#SMS_RETRIEVED_ACTION.
        Task<Void> task = client.startSmsRetriever();

        // Listen for success/failure of the start Task. If in a background thread, this
        // can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                Log.i("", "");
                // ...
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
                // ...
                Log.i("", "");
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        // Shows user name
        ApiManager.getUserProfile(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userProfile -> {
                    if(userProfile != null && !TextUtils.isEmpty(userProfile.getFullName())) {
                        profileBtn.setText(userProfile.getFullName().toUpperCase());
                        premiumLogoBtn.setVisibility(View.GONE);
                        profileBtn.setVisibility(View.VISIBLE);
                    } else if(userProfile != null && !TextUtils.isEmpty(userProfile.getEmailId())) {
                        profileBtn.setText(userProfile.getEmailId().toUpperCase());
                        premiumLogoBtn.setVisibility(View.GONE);
                        profileBtn.setVisibility(View.VISIBLE);
                    } else if(userProfile != null && !TextUtils.isEmpty(userProfile.getContact())) {
                        profileBtn.setText(userProfile.getContact().toUpperCase());
                        premiumLogoBtn.setVisibility(View.GONE);
                        profileBtn.setVisibility(View.VISIBLE);
                    } else {
                        profileBtn.setVisibility(View.GONE);
                        premiumLogoBtn.setVisibility(View.VISIBLE);
                    }
                });
    }
}
