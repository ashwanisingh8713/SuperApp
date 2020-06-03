package com.ns.alerts;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.netoperation.config.model.ImportantMsg;
import com.ns.callbacks.OnDialogBtnClickListener;
import com.ns.thpremium.R;
import com.ns.utils.ResUtil;
import com.ns.view.THP_AutoResizeWebview;
import com.ns.view.text.ArticleTitleTextView;

public class ConfigurationMsgDialog extends DialogFragment {

    private ImportantMsg mImportantMsg;

    public static ConfigurationMsgDialog getInstance(ImportantMsg importantMsg) {
        ConfigurationMsgDialog infoDialog = new ConfigurationMsgDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("importantMsg", importantMsg);
        infoDialog.setArguments(bundle);
        return infoDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImportantMsg = getArguments().getParcelable("importantMsg");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            return inflater.inflate(R.layout.dialog_configuration_update_msg, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
//        getDialog().getWindow().setGravity(Gravity.TOP);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
        //p.x = 200;
        getDialog().getWindow().setAttributes(p);

        ArticleTitleTextView dialogTitle = view.findViewById(R.id.dialogTitle);
        THP_AutoResizeWebview dialogMsgWebView = view.findViewById(R.id.dialogMsg);

        //dialogMsg.setText(ResUtil.htmlText(mImportantMsg.getMsg()));
        dialogTitle.setText(ResUtil.htmlText(mImportantMsg.getTitle()));

        dialogMsgWebView.loadDataWithBaseURL("https:/", THP_AutoResizeWebview.defaultgroup_showDescription(getActivity(), "", mImportantMsg.getMsg()),
                "text/html", "UTF-8", null);

            view.findViewById(R.id.okTxt).setOnClickListener(v -> {
                if (mOnDialogBtnClickListener != null) {
                    mOnDialogBtnClickListener.onDialogOkClickListener();
                }
                dismiss();
            });

            view.findViewById(R.id.cancelTxt).setOnClickListener(v -> {
                if (mOnDialogBtnClickListener != null) {
                    mOnDialogBtnClickListener.onDialogCancelClickListener();
                }
                dismiss();
            });

    }

    private OnDialogBtnClickListener mOnDialogBtnClickListener;

    public void setOnDialogOkClickListener(OnDialogBtnClickListener onPermissionAcceptedListener) {
        mOnDialogBtnClickListener = onPermissionAcceptedListener;
    }

}
