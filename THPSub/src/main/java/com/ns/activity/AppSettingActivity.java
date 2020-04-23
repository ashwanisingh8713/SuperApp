package com.ns.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.main.DFPConsent;
import com.netoperation.util.DefaultPref;
import com.ns.alerts.Alerts;
import com.ns.clevertap.CleverTapUtil;
import com.ns.thpremium.R;
import com.ns.tts.LanguageItem;
import com.ns.tts.TTSPreference;
import com.ns.tts.TTSUtil;
import com.ns.utils.IntentUtil;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppSettingActivity extends BaseAcitivityTHP implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "SettingsFragment";
    private SeekBar mSeekBar;
    private TextView mSizeTextView, mSmallArticleTextView, mMediumArticleTextView,
            mLargeArticleTextView, mXLargeTextView;
    private ImageView mSizeImageView;
    private LinearLayout mSizeLayout;
    private RelativeLayout mArticleSizeLayout;
    private Switch mLocationASwitch;
    private Switch mDayModeASwitch;
    private Switch mPushNotification;
    private Switch mTurnOffImages;

    private TextToSpeech textToSpeech;

    private boolean isDayMode;

    private DFPConsent dfpConsent;


    @Override
    public int layoutRes() {
        return R.layout.activity_setting;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDetailToolbar().showBackTitleIcons("Settings", backBtn->{
           finish();
        });

        mSizeTextView = findViewById(R.id.textView_size);
        mSeekBar = findViewById(R.id.seekbar_font_size);
        mSizeImageView = findViewById(R.id.imageView_size);
        mSizeLayout = findViewById(R.id.layout_font_size);
        mArticleSizeLayout = findViewById(R.id.layout_article_size);
        mSmallArticleTextView = findViewById(R.id.textview_article_small);
        mMediumArticleTextView = findViewById(R.id.textview_article_medium);
        mLargeArticleTextView = findViewById(R.id.textview_article_large);
        mXLargeTextView = findViewById(R.id.textview_article_xlarge);
        mLocationASwitch = findViewById(R.id.switch_location);
        mDayModeASwitch = findViewById(R.id.switch_daymode);
        mPushNotification = findViewById(R.id.switch_push_notification);
        mTurnOffImages = findViewById(R.id.switch_turn_off_image);


        mLocationASwitch.setTypeface(Typeface.createFromAsset(getAssets(), getResources().getString(R.string.THP_TundraOffc)));
        mDayModeASwitch.setTypeface(Typeface.createFromAsset(getAssets(), getResources().getString(R.string.THP_TundraOffc)));
        mPushNotification.setTypeface(Typeface.createFromAsset(getAssets(), getResources().getString(R.string.THP_TundraOffc)));

        isDayMode = DefaultPref.getInstance(AppSettingActivity.this).isUserThemeDay();

        if(isDayMode) {
            mPushNotification.setTextColor(ResUtil.getColor(getResources(), com.ns.thpremium.R.color.article_list_text));
            mDayModeASwitch.setTextColor(ResUtil.getColor(getResources(), com.ns.thpremium.R.color.article_list_text));
            mTurnOffImages.setTextColor(ResUtil.getColor(getResources(), com.ns.thpremium.R.color.article_list_text));
        }
        else {
            mPushNotification.setTextColor(ResUtil.getColor(getResources(), com.ns.thpremium.R.color.dark_article_list_text));
            mDayModeASwitch.setTextColor(ResUtil.getColor(getResources(), com.ns.thpremium.R.color.dark_article_list_text));
            mTurnOffImages.setTextColor(ResUtil.getColor(getResources(), com.ns.thpremium.R.color.dark_article_list_text));
        }


        mSeekBar.incrementProgressBy(1);
        mSeekBar.setMax(3);

        setUserPreferences();

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                resetTextColors();
                /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), "Setting", "Setting Screen: Article font size changed",
                        "Setting Fragment");
                FlurryAgent.logEvent("Setting Screen: Article font size changed");*/
                switch (progress) {
                    case 0:
                        mSizeTextView.setText(getString(R.string.info_article_small));
                        mSmallArticleTextView.setTextColor(getResources().getColor(R.color.color_primary));
                        break;
                    case 1:
                        mSizeTextView.setText(getString(R.string.info_article_medium));
                        mMediumArticleTextView.setTextColor(getResources().getColor(R.color.color_primary));
                        break;
                    case 2:
                        mSizeTextView.setText(getString(R.string.info_article_large));
                        mLargeArticleTextView.setTextColor(getResources().getColor(R.color.color_primary));
                        break;
                    case 3:
                        mSizeTextView.setText(getString(R.string.info_article_xlarge));
                        mXLargeTextView.setTextColor(getResources().getColor(R.color.color_primary));
                        break;

                }
                DefaultPref.getInstance(AppSettingActivity.this).setDescriptionSize(progress + 1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSizeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), "Setting", "Setting Screen: Article font button clicked",
                        "Setting Fragment");
                FlurryAgent.logEvent("Setting Screen: Article font button clicked");*/
                adjustFontSize();
            }
        });

        mArticleSizeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), "Setting", "Setting Screen: Article font button clicked",
                        "Setting Fragment");
                FlurryAgent.logEvent("Setting Screen: Article font button clicked");*/
                adjustFontSize();
            }
        });


        // GDPR Setting Option
        boolean isUserFromEurope = DefaultPref.getInstance(this).isUserFromEurope();
        if(isUserFromEurope) {
            findViewById(R.id.gdprSettingTxt).setVisibility(View.VISIBLE);
            findViewById(R.id.gdprDivider).setVisibility(View.VISIBLE);
            findViewById(R.id.gdprSettingTxt).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DFP_GDPR_CONSENT();
                }
            });
        }


        ttsSettingSetup();

        final TTSPreference ttsPreference = TTSPreference.getInstance(this);
        String displayName = ttsPreference.getDisplayName();

        if(TextUtils.isEmpty(displayName)) {
            languageAvailableVerification(this);
        }

    }

    private RelativeLayout mTtsSettingLayout;
    private TextView tts1TV;
    private TextView tts2TV;

    private void ttsSettingSetup() {

        mTtsSettingLayout = findViewById(R.id.ttsSettingLayout);
        tts1TV = findViewById(R.id.tts1);
        tts2TV = findViewById(R.id.tts2);


        mTtsSettingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TTSUtil.openTTSAppSettingScreen(AppSettingActivity.this);
                //CleverTap Event
                CleverTapUtil.cleverTapEventSettings(AppSettingActivity.this, THPConstants.CT_KEY_Read_aloud, "Yes");
            }
        });

        setTTSValues();

    }


    private void setTTSValues() {
        if (tts2TV != null) {
            final TTSPreference ttsPreference = TTSPreference.getInstance(this);
            tts2TV.setText(ttsPreference.getDisplayName() + ", Speed " + ttsPreference.getSpeedInString() + ", Pitch "+ttsPreference.getPinchSeekbarPos()+"%");
        }
    }

    private void adjustFontSize() {
        if (mSizeLayout.getVisibility() == View.GONE) {
            mSizeLayout.setVisibility(View.VISIBLE);
            //Clever Tap Event
            CleverTapUtil.cleverTapEventSettings(this, THPConstants.CT_KEY_Article_font, "Yes");
        } else {
            mSizeLayout.setVisibility(View.GONE);
            CleverTapUtil.cleverTapEventSettings(this, THPConstants.CT_KEY_Article_font, "No");
        }
    }

    private void setUserPreferences() {
        int progress = DefaultPref.getInstance(this).getDescriptionSize();
        mSeekBar.setProgress(progress - 1);
        switch (progress - 1) {
            case 0:
                mSizeTextView.setText(getString(R.string.info_article_small));
                mSmallArticleTextView.setTextColor(getResources().getColor(R.color.color_primary));
                break;
            case 1:
                mSizeTextView.setText(getString(R.string.info_article_medium));
                mMediumArticleTextView.setTextColor(getResources().getColor(R.color.color_primary));
                break;
            case 2:
                mSizeTextView.setText(getString(R.string.info_article_large));
                mLargeArticleTextView.setTextColor(getResources().getColor(R.color.color_primary));
                break;
            case 3:
                mSizeTextView.setText(getString(R.string.info_article_xlarge));
                mXLargeTextView.setTextColor(getResources().getColor(R.color.color_primary));
                break;

        }

        boolean notificationEnabled = DefaultPref.getInstance(this).isNotificationEnable();
        boolean daymode = DefaultPref.getInstance(this).isUserThemeDay();
        mLocationASwitch.setOnCheckedChangeListener(this);
        mDayModeASwitch.setChecked(!daymode);
        mDayModeASwitch.setOnCheckedChangeListener(this);
        mPushNotification.setChecked(notificationEnabled);
        mPushNotification.setOnCheckedChangeListener(this);
        mTurnOffImages.setOnCheckedChangeListener(this);
    }

    private void resetTextColors() {
        mSmallArticleTextView.setTextColor(getResources().getColor(R.color.text_warm_grey));
        mMediumArticleTextView.setTextColor(getResources().getColor(R.color.text_warm_grey));
        mLargeArticleTextView.setTextColor(getResources().getColor(R.color.text_warm_grey));
        mXLargeTextView.setTextColor(getResources().getColor(R.color.text_warm_grey));

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        switch (compoundButton.getId()) {
            case R.id.switch_daymode:
                /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(this, "Setting", "Setting Screen: Night mode button clicked",
                        "Setting Fragment");
                FlurryAgent.logEvent("Setting Screen: Night mode button clicked");*/

                DefaultPref.getInstance(this).setUserTheme(!isDayMode);

                if (!isDayMode) {
                    //Clever Tap Event
                    CleverTapUtil.cleverTapEventSettings(this, THPConstants.CT_KEY_Night_mode, "No");
                } else {
                    CleverTapUtil.cleverTapEventSettings(this, THPConstants.CT_KEY_Night_mode, "Yes");
                }

                IntentUtil.openMainTabPage(this);

                break;
            case R.id.switch_location:
                /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(this, "Setting", "Setting Screen: Detect location button clicked", "Setting Fragment");
                FlurryAgent.logEvent("Setting Screen: Detect location button clicked");*/
                // TODO, Currently not required
                break;
            case R.id.switch_push_notification:
                /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(this, "Setting", "Setting Screen: Push Notification  button clicked", "Setting Fragment");
                FlurryAgent.logEvent("Setting Screen: Push Notification button clicked");*/
                DefaultPref.getInstance(this).setNotificationEnable(checked);
                //Clever Tap Event
                if (checked) {
                    CleverTapUtil.cleverTapEventSettings(this, THPConstants.CT_KEY_Push_Notification, "Yes");
                } else {
                    CleverTapUtil.cleverTapEventSettings(this, THPConstants.CT_KEY_Push_Notification, "No");
                }
                break;
            case R.id.switch_turn_off_image:
                /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(this, "Setting", "Setting Screen: Turn off images  button clicked", "Setting Fragment");
                FlurryAgent.logEvent("Setting Screen: Turn off images button clicked");*/
                // TODO, Currently not required
                break;
        }

    }

    private void DFP_GDPR_CONSENT() {
        if (dfpConsent != null) {
            dfpConsent.initUserConsentForm(AppSettingActivity.this);
            return;
        }
        dfpConsent = new DFPConsent();
        dfpConsent.GDPR_Testing(this);
        dfpConsent.init(this, true, new DFPConsent.ConsentSelectionListener() {
            @Override
            public void isUserInEurope(boolean isInEurope) {
                if (isInEurope) {
                    dfpConsent.initUserConsentForm(AppSettingActivity.this);
                }
            }

            @Override
            public void consentLoadingError(String errorDescription) {
                Alerts.showToast(AppSettingActivity.this, "consentLoadingError :: " + errorDescription);
            }
        });
    }

    private void languageAvailableVerification(final Context context) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                textToSpeech = new TextToSpeech(context, status -> {
                    final List<LanguageItem> languageList = new ArrayList<>();
                    final Locale[] locales = Locale.getAvailableLocales();

                    if (textToSpeech != null) {
                        for (Locale locale : locales) {
                            int res = textToSpeech.isLanguageAvailable(locale);

                            if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE && locale.getDisplayLanguage().startsWith("Eng")) {

                                int langStat = textToSpeech.isLanguageAvailable(new Locale(locale.getLanguage() + "-" + locale.getCountry()));

                                final LanguageItem item = new LanguageItem();
                                item.country = locale.getCountry();
                                item.language = locale.getLanguage();
                                item.displayName = locale.getDisplayLanguage() + "(" + locale.getDisplayCountry() + ")";
                                item.isExist = langStat == TextToSpeech.LANG_AVAILABLE;

                                if (!languageList.contains(item)) {
                                    languageList.add(item);
                                }
                            }
                        }
                    }


                    final TTSPreference ttsPreference = TTSPreference.getInstance(context);

                    final LanguageItem selectedItem = new LanguageItem();
                    selectedItem.country = ttsPreference.getCountry();
                    selectedItem.language = ttsPreference.getLanguage();
                    selectedItem.displayName = ttsPreference.getDisplayName();

                    final int selectedIndex = languageList.indexOf(selectedItem);
                    if (selectedIndex != -1) {
                        selectedItem.isSelected = true;
                        selectedItem.isExist = true;

                        LanguageItem seleItem = languageList.get(selectedIndex);
                        selectedItem.displayName = seleItem.displayName;
                        selectedItem.language = seleItem.language;
                        selectedItem.country = seleItem.country;

                        languageList.remove(selectedItem);
                        languageList.add(selectedIndex, selectedItem);

                        ttsPreference.setCountry(languageList.get(selectedIndex).country);
                        ttsPreference.setLanguage(languageList.get(selectedIndex).language);
                        ttsPreference.setDisplayName(languageList.get(selectedIndex).displayName);

                    }
                    else if(languageList.size() > 0){
                        ttsPreference.setCountry(languageList.get(0).country);
                        ttsPreference.setLanguage(languageList.get(0).language);
                        ttsPreference.setDisplayName(languageList.get(0).displayName);
                    }

                    setTTSValues();
                });


            }
        }, 500);
    }


    @Override
    protected void onDestroy() {
        if(textToSpeech != null) {
            textToSpeech.stop();
        }
        super.onDestroy();
    }
}
