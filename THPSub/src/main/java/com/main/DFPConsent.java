package com.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.clevertap.android.sdk.CleverTapAPI;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.consent.DebugGeography;
import com.netoperation.util.DefaultPref;
import com.netoperation.util.PremiumPref;
import com.ns.activity.CustomizeHomeScreenActivity;
import com.ns.thpremium.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class DFPConsent {

    public void init(final Context context, final boolean forceForConsentDialog, ConsentSelectionListener mConsentSelectionListener) {

        final ConsentInformation consentInformation = ConsentInformation.getInstance(context);
        String networkCode = "22390678";
        String[] publisherIds = {networkCode};
        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                // User's consent status successfully updated.
                boolean isInEurope = ConsentInformation.getInstance(context).isRequestLocationInEeaOrUnknown();
                boolean isUserFromEurope = DefaultPref.getInstance(context).isUserFromEurope();

                DefaultPref.getInstance(context).setDfpConsentExecuted(true);
                DefaultPref.getInstance(context).setUserFromEurope(isInEurope);
                boolean isUserPreferAdsFree = PremiumPref.getInstance(context).isUserAdsFree();
                if(!isInEurope && !isUserPreferAdsFree) {
                    PremiumPref.getInstance(context).setIsUserAdsFree(false);
                }

                // MO-ENGAGE GDPR
                updateCTGDPR(context, isInEurope);

                if((mConsentSelectionListener != null && !isUserFromEurope) || forceForConsentDialog) {
                    mConsentSelectionListener.isUserInEurope(isInEurope);
                }

            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                // User's consent status failed to update.
                Log.i("","");
                consentInformation.reset();

                if(mConsentSelectionListener != null) {
                    mConsentSelectionListener.consentLoadingError(errorDescription);
                }
            }
        });
    }

    private ConsentForm form;

    public void initUserConsentForm(final Context context) {
        URL privacyUrl = null;
        try {
            privacyUrl = new URL("http://www.thehindu.com/privacypolicy");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // Handle error.
        }
        form = new ConsentForm.Builder(context, privacyUrl)
                .withListener(new ConsentFormListener() {
                    @Override
                    public void onConsentFormLoaded() {
                        // Consent form loaded successfully.
                        if(context instanceof CustomizeHomeScreenActivity) {
                            CustomizeHomeScreenActivity activity = (CustomizeHomeScreenActivity) context;
                            if(!activity.isFinishing()) {
                                form.show();
                            }
                        }/* else if(context instanceof MainActivity) {
                            MainActivity activity = (MainActivity) context;
                            if(!activity.isFinishing()) {
                                form.show();
                            }
                        }*/
                        else {
                            form.show();
                        }
                    }

                    @Override
                    public void onConsentFormOpened() {
                        // Consent form was displayed.
                        Log.i("","");
                    }

                    @Override
                    public void onConsentFormClosed(
                            ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        // Consent form was closed.
                        DefaultPref.getInstance(context).setUserSelectedDfpConsent(true);
                        PremiumPref.getInstance(context).setIsUserAdsFree(userPrefersAdFree);

                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        // Consent form error.
                        Log.i("","");
                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
//                .withAdFreeOption()
                .build();

        form.load();

    }



    public interface ConsentSelectionListener {
        void isUserInEurope(boolean isInEurope);
        void consentLoadingError(String errorDescription);
    }


    public void GDPR_Testing(Context context) {
        if(BuildConfig.DEBUG) {

            ConsentInformation.getInstance(context).setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);


            // RedMi
//            ConsentInformation.getInstance(context).addTestDevice("F10810FD9E05E408D3D77DCEC4CFD6E3");
            // POCO
             ConsentInformation.getInstance(context).addTestDevice("69A27258C3736E220C92E889FD41FB39");
            // Moto
//            ConsentInformation.getInstance(context).addTestDevice("0710ECF00058D88516EC9C67FDAB2D13");
            // Honor
            //ConsentInformation.getInstance(context).addTestDevice("C8A85B2402E3A7B45C66F9D746392FB3");
            /*boolean ENABLE_EEA = SharedPreferenceHelper.isGDPR_EEA(context);
            if (ENABLE_EEA) {
                ConsentInformation.getInstance(context).setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);
            } else {
                ConsentInformation.getInstance(context).setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_NOT_EEA);
            }*/


        }
    }

    public static Bundle GDPRStatusBundle(Context context) {
        Bundle nonPersonalizedAdsReqBundle = null;
        final ConsentStatus consentStatus = ConsentInformation.getInstance(context).getConsentStatus();
        switch (consentStatus) {
            case PERSONALIZED:

                break;

            case NON_PERSONALIZED:
                nonPersonalizedAdsReqBundle = new Bundle();
                nonPersonalizedAdsReqBundle.putString("npa", "1");
                break;

            case UNKNOWN:

                break;
        }
        return nonPersonalizedAdsReqBundle;
    }

    /**
     * Notification GDPR
     * @param context
     * @param isEEA
     */
    private void updateCTGDPR(Context context, boolean isEEA) {
        if(CleverTapAPI.getDefaultInstance(context) == null) {
            return;
        }

        // If true, App can stop sending events to CleverTap
        CleverTapAPI.getDefaultInstance(context).setOptOut(isEEA);
        CleverTapAPI.getDefaultInstance(context).enableDeviceNetworkInfoReporting(!isEEA);

        HashMap<String, Object> profileUpdate = new HashMap<String, Object>();

        // if true, Enable email notifications
        profileUpdate.put("MSG-email", !isEEA); // Disable email notifications in EU
        // if true, Enable push notifications
        profileUpdate.put("MSG-push", !isEEA); // Disable push notifications in EU
        // if true, Enable SMS notifications
        profileUpdate.put("MSG-sms", !isEEA); // Disable SMS notifications in EU
        // if true, Enable push notifications
        // for all devices notifications
        profileUpdate.put("MSG-push-all", !isEEA); //Disable push notifications for all devices in EU

        CleverTapAPI.getDefaultInstance(context).pushProfile(profileUpdate);


    }

}
