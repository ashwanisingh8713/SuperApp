package com.ns.alerts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ns.thpremium.R;

public class PermissionInfoDialog extends DialogFragment {

    private String mFrom;

    public static PermissionInfoDialog getInstance(String from) {
        PermissionInfoDialog infoDialog = new PermissionInfoDialog();
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
            return inflater.inflate(R.layout.dialog_permissioninfo, container);
        } else if(mFrom !=null && mFrom.equalsIgnoreCase("notContinueDialogForPermission")) {
            return inflater.inflate(R.layout.dialog_not_continue_perm_info, container);
        } else {
            return inflater.inflate(R.layout.dialog_permissioninfo, container);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(view.findViewById(R.id.okTxt) != null) {
            view.findViewById(R.id.okTxt).setOnClickListener(v -> {
                if (mOnPermissionAcceptedListener != null) {
                    mOnPermissionAcceptedListener.onDialogOkClickListener();
                    dismiss();
                }
            });
        }

        if(view.findViewById(R.id.cancelTxt) != null) {
            view.findViewById(R.id.cancelTxt).setOnClickListener(v -> {
                dismiss();
            });
        }

        if(view.findViewById(R.id.appInfoTxt) != null) {
            view.findViewById(R.id.appInfoTxt).setOnClickListener(v -> {
                if (mOnDialogAppInfoClickListener != null) {
                    mOnDialogAppInfoClickListener.onDialogAppInfoClickListener();
                    dismiss();
                }
            });
        }
    }

    private OnDialogOkClickListener mOnPermissionAcceptedListener;
    private OnDialogAppInfoClickListener mOnDialogAppInfoClickListener;

    public void setOnDialogOkClickListener(OnDialogOkClickListener onPermissionAcceptedListener) {
        mOnPermissionAcceptedListener = onPermissionAcceptedListener;
    }

    public void setOnDialogAppInfoClickListener(OnDialogAppInfoClickListener onDialogInfoClickListener) {
        mOnDialogAppInfoClickListener = onDialogInfoClickListener;
    }

    public interface OnDialogOkClickListener {
        void onDialogOkClickListener();
    }

    public interface OnDialogAppInfoClickListener {
        void onDialogAppInfoClickListener();
    }
}
