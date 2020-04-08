package com.ns.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;
import com.netoperation.retrofit.ServiceFactory;
import com.netoperation.util.UserPref;
import com.ns.callbacks.FragmentTools;
import com.ns.callbacks.ToolbarClickListener;
import com.ns.model.ToolbarCallModel;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.tts.TTSManager;
import com.ns.utils.CommonUtil;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.DetailToolbar;
import com.ns.view.ListingToolbar;

public abstract class BaseAcitivityTHP extends AppCompatActivity implements ToolbarClickListener {

    public abstract int layoutRes();

    protected FragmentTools mFragmentTools;

    /** This holds Toolbar object*/
    private Toolbar mToolbar;

    public DetailToolbar getDetailToolbar() {
        return (DetailToolbar) mToolbar;
    }

    public ListingToolbar getListingToolbar() {
        return (ListingToolbar) mToolbar;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    protected boolean mIsDayTheme = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        mIsDayTheme = UserPref.getInstance(this).isUserThemeDay();

        // Dialog Theme change
        if(mIsDayTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //For day mode theme for dialog
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); //For night mode theme for dialog
        }
        //Assigning base url
        if(BuildConfig.IS_PRODUCTION) {
            ServiceFactory.BASE_URL = BuildConfig.PRODUCTION_BASE_URL;
        } else {
            ServiceFactory.BASE_URL = BuildConfig.STATGGING_BASE_URL;
        }
        super.onCreate(savedInstanceState);
        setContentView(layoutRes());

        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle(" ");
            mToolbar.setTitle(null);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            // Registering Back button click listener
            if(mToolbar instanceof DetailToolbar) {
                DetailToolbar detailToolbar = (DetailToolbar) mToolbar;
                detailToolbar.setToolbarMenuListener(this);
            }

        }
    }

    @Override
    protected void onStop() {
        TTSManager.getInstance().stopTTS();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        TTSManager.getInstance().release();
        super.onDestroy();
    }


    public void setOnFragmentTools(FragmentTools fragmentTools) {
        mFragmentTools = fragmentTools;
    }

    @Override
    public void onBackClickListener() {
        FragmentManager fm = getSupportFragmentManager();
        int fragmentCount = fm.getBackStackEntryCount();
        if (fragmentCount > 0) {
            fm.popBackStack();
        } else {
            finish();
//            IntentUtil.exitActivityAnim(this);
        }

        if(mToolbar!=null) {
            CommonUtil.hideKeyboard(mToolbar);
        }

    }


    @Override
    public void onSearchClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFragmentTools != null) {
            mFragmentTools.onSearchClickListener(toolbarCallModel);
        }
    }

    @Override
    public void onShareClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFragmentTools != null) {
            mFragmentTools.onShareClickListener(toolbarCallModel);
        }
    }

    @Override
    public void onCreateBookmarkClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFragmentTools != null) {
            mFragmentTools.onCreateBookmarkClickListener(toolbarCallModel);
        }
    }

    @Override
    public void onFontSizeClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFragmentTools != null) {
            mFragmentTools.onFontSizeClickListener(toolbarCallModel);
        }
    }

    @Override
    public void onCommentClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFragmentTools != null) {
            mFragmentTools.onCommentClickListener(toolbarCallModel);
        }
    }

    @Override
    public void onTTSPlayClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFragmentTools != null) {
            mFragmentTools.onTTSPlayClickListener(toolbarCallModel);
        }
    }

    @Override
    public void onTTSStopClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFragmentTools != null) {
            mFragmentTools.onTTSStopClickListener(toolbarCallModel);
        }
    }

    @Override
    public void onRemoveBookmarkClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFragmentTools != null) {
            mFragmentTools.onRemoveBookmarkClickListener(toolbarCallModel);
        }
    }

    @Override
    public void onFavClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFragmentTools != null) {
            mFragmentTools.onFavClickListener(toolbarCallModel);
        }
    }

    @Override
    public void onLikeClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFragmentTools != null) {
            mFragmentTools.onLikeClickListener(toolbarCallModel);
        }
    }


    public void noConnectionSnackBar(View view) {
        if(view == null) {
            return;
        }
        Snackbar mSnackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout mSnackbarView = (Snackbar.SnackbarLayout) mSnackbar.getView();
        mSnackbarView.setBackgroundColor(Color.BLACK);
        TextView textView = mSnackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        TextView textView1 = mSnackbarView.findViewById(com.google.android.material.R.id.snackbar_action);
        textView.setVisibility(View.INVISIBLE);
        textView1.setVisibility(View.INVISIBLE);
        View snackView = getLayoutInflater().inflate(R.layout.thp_noconnection_snackbar, null);
        snackView.findViewById(R.id.action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        });
        mSnackbarView.addView(snackView);
        mSnackbar.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(this, "BaseAcitivityTHP Screen", BaseAcitivityTHP.class.getSimpleName());
    }
}
