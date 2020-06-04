package com.ns.alerts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ns.callbacks.OnDialogBtnClickListener;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.view.text.ArticleTitleTextView;
import com.ns.view.text.CustomTextView;

public class HomePermissionInfoDialog extends DialogFragment {

    public static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private String mFrom;

    public static HomePermissionInfoDialog getInstance(String from) {
        HomePermissionInfoDialog infoDialog = new HomePermissionInfoDialog();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        infoDialog.setArguments(bundle);
        return infoDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFrom = getArguments().getString("from");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(mFrom !=null && mFrom.equalsIgnoreCase("defaultInfoDialogForPermission")) {
            return inflater.inflate(R.layout.dialog_home_permissioninfo, container);
        } else if(mFrom !=null && mFrom.equalsIgnoreCase("notContinueDialogForPermission")) {
            return inflater.inflate(R.layout.dialog_home_not_continue_perm_info, container);
        } else {
            return inflater.inflate(R.layout.dialog_home_permissioninfo, container);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArticleTitleTextView dialogTitle = view.findViewById(R.id.dialogTitle);
        CustomTextView informationTextView = view.findViewById(R.id.informationTextView);
        if(dialogTitle != null) {
            dialogTitle.setText(getResources().getString(R.string.APP_NAME));
        }
        if (informationTextView != null) {
            informationTextView.setText(getString(R.string.the_hindu_requires_the_following_information, getString(R.string.APP_NAME)));
        }

        if(view.findViewById(R.id.okTxt) != null) {
            view.findViewById(R.id.okTxt).setOnClickListener(v -> {
                if (mOnDialogBtnClickListener != null) {
                    mOnDialogBtnClickListener.onDialogOkClickListener();
                }
                dismiss();
            });
        }

        if(view.findViewById(R.id.cancelTxt) != null) {
            view.findViewById(R.id.cancelTxt).setOnClickListener(v -> {
                if (mOnDialogBtnClickListener != null) {
                    mOnDialogBtnClickListener.onDialogCancelClickListener();
                }
                dismiss();
            });
        }

        if(view.findViewById(R.id.appInfoTxt) != null) {
            view.findViewById(R.id.appInfoTxt).setOnClickListener(v -> {
                if (mOnDialogBtnClickListener != null) {
                    mOnDialogBtnClickListener.onDialogAppInfoClickListener();
                }
                dismiss();
            });
        }
    }

    private OnDialogBtnClickListener mOnDialogBtnClickListener;

    public void setOnDialogOkClickListener(OnDialogBtnClickListener onPermissionAcceptedListener) {
        mOnDialogBtnClickListener = onPermissionAcceptedListener;
    }




}
