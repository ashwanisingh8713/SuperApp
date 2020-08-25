package com.ns.view;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.ns.thpremium.R;
import com.ns.utils.CommonUtil;
import com.ns.utils.StringUtils;

public class EmailTextChangedListener implements TextWatcher {

    private EditText mEmailOrMobile_Et;
    private TextView mTextViewError;


    private final String TAG = "NONONo";

    public EmailTextChangedListener(EditText emailOrMobile_Et, TextView textViewError) {
        this.mEmailOrMobile_Et = emailOrMobile_Et;
        this.mTextViewError = textViewError;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            if(mTextViewError != null) {
                mTextViewError.setText("");
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (mEmailOrMobile_Et.getText().toString().length() > 1) {
            mEmailOrMobile_Et.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_clear, 0);
        } else {
            mEmailOrMobile_Et.setCompoundDrawablesWithIntrinsicBounds(0,0,0, 0);
            if(mTextViewError != null) {
                mTextViewError.setText("");
            }
        }

        mEmailOrMobile_Et.setFilters(new InputFilter[] { new InputFilter.LengthFilter(50) });

    }
}
