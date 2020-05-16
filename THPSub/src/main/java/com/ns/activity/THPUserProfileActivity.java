package com.ns.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.netoperation.model.PaytmModel;
import com.netoperation.model.TxnDataBean;
import com.netoperation.model.UserProfile;
import com.netoperation.net.ApiManager;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.util.PremiumPref;
import com.ns.alerts.Alerts;
import com.ns.callbacks.OnPlanInfoLoad;
import com.ns.callbacks.OnSubscribeBtnClick;
import com.ns.callbacks.OnSubscribeEvent;
import com.ns.clevertap.CleverTapUtil;
import com.ns.loginfragment.AccountCreatedFragment;
import com.ns.loginfragment.RecoPlansWebViewFragment;
import com.ns.loginfragment.SubscriptionStep_3_Fragment;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.userprofilefragment.TxnStatusFragment;
import com.ns.userprofilefragment.UserProfileFragment;
import com.ns.utils.FragmentUtil;
import com.ns.utils.IntentUtil;
import com.ns.utils.NetUtils;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class THPUserProfileActivity extends AppLocationActivity implements OnSubscribeBtnClick, OnPlanInfoLoad, RecoPlansWebViewFragment.SubsPlanSelectListener {

    private String mSelectedPlanId,mpackValidity,mpackName;
    int mpackValue;
    private static final int INAPP_SUBSCRIPTION_REQUEST_CODE = 10001;
    public static final int PAYMENT_ICICI_REQ_CODE = 201;
    //private IabHelper mHelper;

    private OnSubscribeEvent mOnSubscribeEvent;
    private UserProfile mUserProfile;
    private TxnDataBean mSelectedBean;
    private ProgressDialog progress;
    private String mCountryName = "India";
    public static final String START_TIME_KEY = "startTimeKey";


    public void setOnSubscribeEvent(OnSubscribeEvent onSubscribeEvent) {
        mOnSubscribeEvent = onSubscribeEvent;
    }

    @Override
    public int layoutRes() {
        return R.layout.activity_userprofile;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String from = getIntent().getExtras().getString("from");

        if (from != null && from.equalsIgnoreCase(THPConstants.FROM_SUBSCRIPTION_EXPLORE) || from.equalsIgnoreCase(THPConstants.FROM_NOTIFICATION_SUBSCRIPTION_EXPLORE)) {
            //SubscriptionStep_1_Fragment fragment = SubscriptionStep_1_Fragment.getInstance(from);
            //Filter plan Offer Type
            String planOffer = getIntent().getExtras().getString("planOffer");
            RecoPlansWebViewFragment fragment = RecoPlansWebViewFragment.getInstance(from, planOffer);
            FragmentUtil.replaceFragmentAnim(this, R.id.parentLayout, fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, true);
        } else if (from != null && from.equalsIgnoreCase(THPConstants.FROM_USER_PROFILE)) {

            // Uncomment below code snippet
            UserProfileFragment fragment = UserProfileFragment.getInstance("");
            FragmentUtil.replaceFragmentAnim(this, R.id.parentLayout, fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, true);

        } else if (from != null && from.equalsIgnoreCase(THPConstants.FROM_USER_SignUp)) {

            // Uncomment below code snippet
            UserProfileFragment fragment = UserProfileFragment.getInstance("");
            FragmentUtil.replaceFragmentAnim(this, R.id.parentLayout, fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, true);

            AccountCreatedFragment accountCreated = AccountCreatedFragment.getInstance("");
            FragmentUtil.addFragmentAnim(this, R.id.parentLayout, accountCreated, FragmentUtil.FRAGMENT_NO_ANIMATION, false);

        } else if (from != null && from.equalsIgnoreCase(THPConstants.FROM_USER_ACCOUNT_CREATED)) {
            SubscriptionStep_3_Fragment fragment = SubscriptionStep_3_Fragment.getInstance(THPConstants.FROM_USER_ACCOUNT_CREATED);
            FragmentUtil.replaceFragmentAnim(this, R.id.parentLayout, fragment,
                    FragmentUtil.FRAGMENT_ANIMATION, true);
        }

        // Gets User Profile Data
        ApiManager.getUserProfile(this).subscribe(userProfile -> {
            mUserProfile = userProfile;
        });

    }

    private List<String> mPlanIds = new ArrayList<>();


    @Override
    public void onPlansLoaded(List<TxnDataBean> planInfoList) {
        for (TxnDataBean bean : planInfoList) {
            mPlanIds.add(bean.getPlanId());
        }
        //initIabHelper();
    }

    /*private void initIabHelper() {
        mHelper = new IabHelper(THPUserProfileActivity.this, BuildConfig.SUBSCRIPTION_BASE64);
        mHelper.enableDebugLogging(true);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                Log.i("", "");

                if (result.isSuccess()) {
                    try {
                        Inventory inventory = mHelper.queryInventory(true, mPlanIds);

                        Log.i("", "");
                    } catch (IabException e) {
                        e.printStackTrace();
                        Log.i("", "");
                    }
                }
            }
        });

    }*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INAPP_SUBSCRIPTION_REQUEST_CODE) {
            //mHelper.handleActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                if (mOnSubscribeEvent != null) {
                    mOnSubscribeEvent.onSubscribeEvent(true);
                }
            } else if (resultCode == RESULT_CANCELED) {
                if (mOnSubscribeEvent != null) {
                    mOnSubscribeEvent.onSubscribeEvent(false);
                }
            }
        } else if (requestCode == PAYMENT_ICICI_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                //Load user info details whenever returned from payment page
                //getUserInfoApiCall();
                showProgressDialog("\nVerifying transaction status ...");
                ApiManager.getUserInfo(this, mUserProfile.getAuthorization(), BuildConfig.SITEID,
                        ResUtil.getDeviceId(this), mUserProfile.getUserId(),
                        PremiumPref.getInstance(this).getLoginTypeId(),
                        PremiumPref.getInstance(this).getLoginPasswd())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(val -> {
                            hideProgressDialog();
                            TxnStatusFragment fragment = TxnStatusFragment.getInstance("success", "");
                            FragmentUtil.replaceFragmentAnim(this, R.id.parentLayout, fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, true);
                            //MP Firebase & CleverTap events
                            if (THPConstants.IS_FROM_MP_BLOCKER) {
                                DefaultTHApiManager.getMPTableObject(THPUserProfileActivity.this)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(mpTable -> {
                                            CleverTapUtil.cleverTapConversionFromMP(THPUserProfileActivity.this, mpTable.getCycleName());
                                            THPFirebaseAnalytics.firebaseConversionFromMP(THPUserProfileActivity.this, mpTable.getCycleName());
                                        },throwable -> {
                                            throwable.printStackTrace();
                                        });
                            }
                        }, throwable -> {
                            hideProgressDialog();
                            TxnStatusFragment fragment = TxnStatusFragment.getInstance("pending", "Verifying transaction failed");
                            FragmentUtil.replaceFragmentAnim(THPUserProfileActivity.this, R.id.parentLayout, fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
                        });

            } else if (resultCode == RESULT_CANCELED) {
                //Load user info details whenever returned from payment page
                if (data != null) {
                    getUserInfoApiCall();
                    TxnStatusFragment fragment = TxnStatusFragment.getInstance(data.getStringExtra("status"), data.getStringExtra("message"));
                    FragmentUtil.replaceFragmentAnim(this, R.id.parentLayout, fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
                }
            }
        }  else if (requestCode == 100) {
            // Gets User Profile Data and Check payment redirection from @SignInAndSignUpActivity
            ApiManager.getUserProfile(this)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(userProfile -> {
                        mUserProfile = userProfile;
                        if (data != null && data.getBooleanExtra("isRequestPayment", false)) {
                            //Start Payment
                            onSubsPlanSelected(mSelectedPlanId, mCountryName,mpackValue,mpackValidity,mpackName);
                        }
            });

        }
    }

    @Override
    public void onSubscribeBtnClick(TxnDataBean bean) { /*Not using anymore*/
        mSelectedPlanId = bean.getPlanId();
        if (!PremiumPref.getInstance(this).isUserLoggedIn()/*mUserProfile == null || mUserProfile.getUserId() == null || TextUtils.isEmpty(mUserProfile.getUserId())*/) {
            IntentUtil.openSignInOrUpActivity(this, THPConstants.FROM_SignUpAndPayment);

            return;
        }

        /*if (mHelper != null) {
            mHelper.flagEndAsync();
        }*/
        mSelectedBean = bean;
        /*IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
                = new IabHelper.OnIabPurchaseFinishedListener() {
            public void onIabPurchaseFinished(IabResult result,
                                              Purchase purchase) {

                Log.i("", "");
                if (result.isFailure()) {
                    //Handle error
                    Alerts.showAlertDialogOKBtn(THPUserProfileActivity.this, "Purchase Error!", result.getMessage());
                    return;
                } else if (purchase.getSku().equals(mSelectedPlanId)) {
                    loadInventory();
                    // buyButton.setEnabled(false);
                    //Submit subscription purchase details
                    //createSubscription(purchase);
                }

            }
        };*/

        //boolean isSubscription = mHelper.subscriptionsSupported();
        Log.i("", "");
        //mHelper.launchSubscriptionPurchaseFlow(THPUserProfileActivity.this, mSelectedPlanId, INAPP_SUBSCRIPTION_REQUEST_CODE,
        //        mPurchaseFinishedListener);

        //Testing create subscription
        //createSubscription();

        if (NetUtils.isConnected(this)) {
            //Generate ChecksumHash
            //generateChecksumHashForPaytm(8/*Get the planId during runtime*/);
            //ICICI payment gateway
            //iciciPaymentIntegration("8", "united states");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select payment method (For testing purpose)");
            String[] items = new String[]{"Paytm","ICICI"};
            builder.setItems(items, (dialogInterface, i) -> {
                dialogInterface.dismiss();
                if (i == 0) {
                    generateChecksumHashForPaytm(Integer.parseInt(mSelectedPlanId/*Get the planId during runtime*/));
                } else {
                    iciciPaymentIntegration(mSelectedPlanId, "united states");
                }
            });
            builder.create().show();
        } else {
            Alerts.showSnackbar(this, getResources().getString(R.string.please_check_ur_connectivity));
        }
    }

    @SuppressLint("CheckResult")
    //To be use in Google In-App purchase payment
    private void createSubscription(String txnId, String billingChannel) {
        String email = mUserProfile.getEmailId();
        String contact = mUserProfile.getContact();
        String prefContact = email;
        if (email == null || TextUtils.isEmpty(email)) {
            prefContact = contact;
        }

        String currency = "inr";
        if (mSelectedBean.getAmount() == 0.0) {
            currency = null;
        }
        ApiManager.createSubscription(mUserProfile.getAuthorization(), mUserProfile.getUserId(), txnId, "" + mSelectedBean.getAmount(), "WAP", BuildConfig.SITEID,
                mSelectedBean.getPlanId(), "Subscription", billingChannel, mSelectedBean.getValidity(),
                prefContact, currency, createTaxJsonObject().toString(), "" + mSelectedBean.getNetamount())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(keyValueModel -> {
                    //Fail and success conditional implementation
                    if (keyValueModel.getName().equals("Fail")) {
                        Alerts.showErrorDailog(getSupportFragmentManager(),
                                getString(R.string.api_create_subscription_failure_title),
                                getString(R.string.api_create_subscription_failure_message));
                    } else {
                        Alerts.showErrorDailog(getSupportFragmentManager(),
                                getString(R.string.api_create_subscription_success_title),
                                getString(R.string.api_create_subscription_success_message));
                    }

                }, throwable -> {
                    //Handle Error and network interruption
                    Log.i("THPUserProfileActivity", throwable.getMessage());
                    Alerts.showErrorDailog(getSupportFragmentManager(),
                            getString(R.string.api_create_subscription_failure_title),
                            getString(R.string.api_create_subscription_failure_message));
                });
    }

    private JSONObject createTaxJsonObject() {
        JSONObject jsonTax = new JSONObject();
        try {
            jsonTax.accumulate("state", mUserProfile.getAddress_state());
            jsonTax.accumulate("stCode", "");
            JSONObject gstJson = new JSONObject();
            gstJson.accumulate("igst", 0);
            gstJson.accumulate("igst_amt", 0);
            gstJson.accumulate("cgst", 0);
            gstJson.accumulate("cgst_amt", 0);
            gstJson.accumulate("sgst", 0);
            gstJson.accumulate("sgst_amt", 0);
            gstJson.accumulate("utgst", 0);
            gstJson.accumulate("utgst_amt", 0);
            //Put gstJson in jsonTax
            jsonTax.accumulate("gst", gstJson);
        } catch (Exception ignore) {
        }

        return jsonTax;
    }


    /*public void loadInventory() {
        IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
                = new IabHelper.QueryInventoryFinishedListener() {
            public void onQueryInventoryFinished(IabResult result,
                                                 Inventory inventory) {

                Log.i("", "");

                if (result.isFailure()) {
                    // Handle failure
                } else {
                    mHelper.consumeAsync(inventory.getPurchase(mSelectedPlanId),
                            mConsumeFinishedListener);
                }
            }
        };
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }*/


    /*IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    Log.i("", "");

                    if (result.isSuccess()) {
                        //clickButton.setEnabled(true);
                    } else {
                        //handle error
                    }
                }
            };*/

    public void showProgressDialog(String message) {
        if (progress == null) {
            progress = new ProgressDialog(this);
        }
        progress.setTitle(getString(R.string.please_wait));
        progress.setMessage(message);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();
    }

    public void hideProgressDialog() {
        if (progress != null) {
            if (progress.isShowing()) {
                progress.hide();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }

    //Paytm Checksum
    private void generateChecksumHashForPaytm(int planId) {
        if (BuildConfig.DEBUG){
            PremiumPref.getInstance(this).setStatusTest("");
            if (PremiumPref.getInstance(this).getStatus().equalsIgnoreCase("success")) {
                TxnStatusFragment fragment = TxnStatusFragment.getInstance("success", "");
                FragmentUtil.replaceFragmentAnim(THPUserProfileActivity.this, R.id.parentLayout, fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, true);
            } else if (PremiumPref.getInstance(this).getStatus().equalsIgnoreCase("failed")){
                TxnStatusFragment fragment = TxnStatusFragment.getInstance("failed", "");
                FragmentUtil.replaceFragmentAnim(THPUserProfileActivity.this, R.id.parentLayout, fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
            } else if (PremiumPref.getInstance(this).getStatus().equalsIgnoreCase("pending")){
                TxnStatusFragment fragment = TxnStatusFragment.getInstance("pending", "");
                FragmentUtil.replaceFragmentAnim(THPUserProfileActivity.this, R.id.parentLayout, fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
            }
            if (!PremiumPref.getInstance(this).getStatus().equalsIgnoreCase("")) {
                return;
            }
        }
        showProgressDialog("\nInitiating payment request...");

        String PAYMENT_REDIRECT;
        if(BuildConfig.IS_PRODUCTION) {
            PAYMENT_REDIRECT = BuildConfig.PRODUCTION_PAYMENT_REDIRECT;
        } else {
            PAYMENT_REDIRECT = BuildConfig.STATGGING_PAYMENT_REDIRECT;
        }


        ApiManager.generateChecksumHash(PAYMENT_REDIRECT, mUserProfile.getUserId(), planId, mUserProfile.getUserEmailOrContact(), "india" /*Get country code from reco plan info*/)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(paytmModel -> {
                    hideProgressDialog();
                    mEndTime = System.currentTimeMillis();
                    //Fail and success conditional implementation
                    if (paytmModel == null) {
                        TxnStatusFragment fragment = TxnStatusFragment.getInstance("failed", "Payment request failed. "+getString(R.string.please_check_ur_connectivity));
                        FragmentUtil.replaceFragmentAnim(THPUserProfileActivity.this, R.id.parentLayout, fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
                      //  CleverTapUtil.cleverTapEventPaymentStatus(this,"failed",mpackValue,mpackValidity,mpackName,mStartTime,mEndTime);
                        THPFirebaseAnalytics.setFirbasePaymentSuccessFailedEvent(THPUserProfileActivity.this,"Action","failed",mpackValue,mpackValidity,mpackName,mStartTime,mEndTime);
                    } else {
                        paytmIntegration(paytmModel);
                    }
                }, throwable -> {
                    hideProgressDialog();
                    //Handle Error and network interruption
                    Log.i("THPUserProfileActivity", throwable.getMessage());
                    TxnStatusFragment fragment = TxnStatusFragment.getInstance("failed", "Payment request failed. "+getString(R.string.please_check_ur_connectivity));
                    FragmentUtil.replaceFragmentAnim(THPUserProfileActivity.this, R.id.parentLayout, fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
                  //  CleverTapUtil.cleverTapEventPaymentStatus(this,"failed",mpackValue,mpackValidity,mpackName,mStartTime,mEndTime);
                    THPFirebaseAnalytics.setFirbasePaymentSuccessFailedEvent(THPUserProfileActivity.this,"Action","failed",mpackValue,mpackValidity,mpackName,mStartTime,mEndTime);
                });
    }

    //Paytm Verify
    private void verifyPaytmTransactionStatus(Bundle bundle) {
        showProgressDialog("\nVerifying transaction status ...");


        String PAYMENT_VERIFY;
        if(BuildConfig.IS_PRODUCTION) {
            PAYMENT_VERIFY = BuildConfig.PRODUCTION_PAYMENT_VERIFY;
        } else {
            PAYMENT_VERIFY = BuildConfig.STATGGING_PAYMENT_VERIFY;
        }

        ApiManager.verifyPaytmTransactionStatus(PAYMENT_VERIFY,bundle)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(paytmTransactionStatus -> {
                    //hideProgressDialog();
                    //getUserInfoApiCall();
                    ApiManager.getUserInfo(this, mUserProfile.getAuthorization(), BuildConfig.SITEID,
                            ResUtil.getDeviceId(this), mUserProfile.getUserId(),
                            PremiumPref.getInstance(this).getLoginTypeId(),
                            PremiumPref.getInstance(this).getLoginPasswd())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(val -> {
                                hideProgressDialog();
                                Log.i("Status", ""+val);
                                TxnStatusFragment fragment;
                                boolean isRoot = true;
                                mEndTime = System.currentTimeMillis();
                                //Fail and success conditional implementation
                                if (paytmTransactionStatus == null) {
                                    fragment = TxnStatusFragment.getInstance("pending", "Verifying transaction status failed in server");
                                    isRoot = false;
                                } else if (paytmTransactionStatus.STATUS.equalsIgnoreCase("TXN_SUCCESS")) {
                                    fragment = TxnStatusFragment.getInstance("success", "");
                                   // CleverTapUtil.cleverTapEventPaymentStatus(this,"success",mpackValue,mpackValidity,mpackName,mStartTime,mEndTime);
                                    THPFirebaseAnalytics.setFirbasePaymentSuccessFailedEvent(THPUserProfileActivity.this,"Action","success",mpackValue,mpackValidity,mpackName,mStartTime,mEndTime);

                                    //MP Firebase & CleverTap events
                                    if (THPConstants.IS_FROM_MP_BLOCKER) {
                                        DefaultTHApiManager.getMPTableObject(THPUserProfileActivity.this)
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(mpTable -> {
                                                    CleverTapUtil.cleverTapConversionFromMP(THPUserProfileActivity.this, mpTable.getCycleName());
                                                    THPFirebaseAnalytics.firebaseConversionFromMP(THPUserProfileActivity.this, mpTable.getCycleName());
                                                },throwable -> {
                                                    throwable.printStackTrace();
                                                });
                                    }

                                } else if (paytmTransactionStatus.STATUS.equalsIgnoreCase("TXN_FAILURE")) {
                                    fragment = TxnStatusFragment.getInstance("failed", paytmTransactionStatus.RESPMSG);
                                    isRoot = false;
                                    //CleverTapUtil.cleverTapEventPaymentStatus(this,"failed",mpackValue,mpackValidity,mpackName,mStartTime,mEndTime);
                                    THPFirebaseAnalytics.setFirbasePaymentSuccessFailedEvent(THPUserProfileActivity.this,"Action","failed",mpackValue,mpackValidity,mpackName,mStartTime,mEndTime);
                                } else {
                                    fragment = TxnStatusFragment.getInstance("pending", paytmTransactionStatus.RESPMSG);
                                    isRoot = false;
                                }
                                FragmentUtil.replaceFragmentAnim(THPUserProfileActivity.this, R.id.parentLayout, fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, isRoot);

                            });
                }, throwable -> {
                    hideProgressDialog();
                    //Handle Error and network interruption
                    Log.i("THPUserProfileActivity", throwable.getMessage());
                    TxnStatusFragment fragment = TxnStatusFragment.getInstance("pending", "Verifying transaction failed");
                    FragmentUtil.replaceFragmentAnim(THPUserProfileActivity.this, R.id.parentLayout, fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
                });
    }

    private void getUserInfoApiCall() {
        ApiManager.getUserInfo(this, mUserProfile.getAuthorization(), BuildConfig.SITEID,
                ResUtil.getDeviceId(this), mUserProfile.getUserId(),
                PremiumPref.getInstance(this).getLoginTypeId(),
                PremiumPref.getInstance(this).getLoginPasswd())
                .subscribe();
    }

    private void paytmIntegration(PaytmModel paytmModel) {
        //1. Application Call Server API to generate checksumhash
        //2. Pass the checksumhash to Paytm SDK to initiate payment
        //3. On Transaction completion , verify the checksumhash coming in response by calling to in server Transaction Status API
        //4. Server verifies order value and transaction status and then send the information to Application
        PaytmPGService service;
        service = PaytmPGService.getStagingService();
        if (BuildConfig.IS_PRODUCTION) {
            service = PaytmPGService.getProductionService();
        } else {
            service = PaytmPGService.getStagingService();
        }
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("CALLBACK_URL", paytmModel.CALLBACK_URL);
        paramMap.put("CHANNEL_ID", paytmModel.CHANNEL_ID);
        paramMap.put("CUST_ID", paytmModel.CUST_ID);
        paramMap.put("INDUSTRY_TYPE_ID", paytmModel.INDUSTRY_TYPE_ID);
        paramMap.put("MID", paytmModel.MID);
        paramMap.put("WEBSITE", paytmModel.WEBSITE);
        paramMap.put("ORDER_ID", paytmModel.ORDER_ID);
        paramMap.put("TXN_AMOUNT", paytmModel.TXN_AMOUNT);
        paramMap.put("CHECKSUMHASH", paytmModel.CHECKSUMHASH);
        PaytmOrder Order = new PaytmOrder(paramMap);
        //Initialize Service object
        service.initialize(Order, null);
        service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
            /*Call Backs*/
            public void someUIErrorOccurred(String inErrorMessage) {
                /*Display the error message as below */
                Toast.makeText(getApplicationContext(), "UI Error : " + inErrorMessage, Toast.LENGTH_LONG).show();
            }

            public void onTransactionResponse(Bundle inResponse) {
                //Verify transaction
                verifyPaytmTransactionStatus(inResponse);
            }

            public void networkNotAvailable() {
                /*Display the message as below */
                Alerts.showAlertDialogOKBtn(THPUserProfileActivity.this, "Network error", "Check your internet connectivity.");
            }

            public void clientAuthenticationFailed(String inErrorMessage) {
                /*Display the message as below */
                Alerts.showAlertDialogOKBtn(THPUserProfileActivity.this, "Authentication failed", "Server error : " + inErrorMessage);
            }

            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                /*Display the message as below */
                Alerts.showAlertDialogOKBtn(THPUserProfileActivity.this, "Webpage loading error", "Unable to load web page : " + inErrorMessage + "(" + iniErrorCode + ")");
            }

            public void onBackPressedCancelTransaction() {
                /*Display the message as below */
                Alerts.showToastAtCenter(getApplicationContext(), "Transaction cancelled");
            }

            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                /*Display the message as below */
                TxnStatusFragment fragment = TxnStatusFragment.getInstance("failed", "Transaction cancelled : "+inErrorMessage);
                FragmentUtil.replaceFragmentAnim(THPUserProfileActivity.this, R.id.parentLayout, fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
            }
        });

    }

    private long mStartTime = 0L;
    private long mEndTime = 0L;

    @Override
    public void onSubsPlanSelected(String planId, String countryName,int packValue,String packValidity,String packName) {
        mSelectedPlanId = planId;
        mCountryName = countryName;
        mStartTime = System.currentTimeMillis();
        mpackName = packName;
        mpackValue = packValue;
        mpackValidity = packValidity;

        if (!PremiumPref.getInstance(this).isUserLoggedIn()) {
            IntentUtil.openSignInOrUpActivity(this, THPConstants.FROM_SignUpAndPayment);
            return;
        }
        if (NetUtils.isConnected(this)) {
            //Don't ask Payment option. Redirect based on country name.
            //If India, PAYTM, else ICICI
            /* Test both payment gateway*/
            //ToDo Comment for Release
            /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select payment method (For testing purpose)");
            String[] items = new String[]{"Paytm","ICICI"};
            builder.setItems(items, (dialogInterface, i) -> {
                dialogInterface.dismiss();
                if (i == 0) {
                    generateChecksumHashForPaytm(Integer.parseInt(mSelectedPlanId));
                } else {
                    iciciPaymentIntegration(mSelectedPlanId, mCountryName);
                }
            });
            builder.create().show();*/
            //ToDo Uncomment for Release
            if (countryName.equalsIgnoreCase("india") || countryName.equalsIgnoreCase("IN")) {
                generateChecksumHashForPaytm(Integer.parseInt(planId));
            } else {
                iciciPaymentIntegration(mSelectedPlanId, countryName);
            }
        } else {
            Alerts.showSnackbar(this, getResources().getString(R.string.please_check_ur_connectivity));
        }

    }

    private void iciciPaymentIntegration(String selectedPlanId, String countryName) {
        Intent intent = new Intent(this, IciciPGActivity.class);
        intent.putExtra("userId", mUserProfile.getUserId());
        intent.putExtra("contact", mUserProfile.getUserEmailOrContact());
        intent.putExtra("planId", selectedPlanId);
        intent.putExtra("country", countryName);
        intent.putExtra("startTime", mStartTime);
        startActivityForResult(intent, PAYMENT_ICICI_REQ_CODE);
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for(Fragment f : fragments){
            if(f instanceof RecoPlansWebViewFragment) {
                if (!((RecoPlansWebViewFragment) f).onBackPressed()){
                    return;
                }
            }
        }
        if (getIntent() != null) {
            String from = getIntent().getExtras().getString("from");
            if (from != null && from.equalsIgnoreCase(THPConstants.FROM_NOTIFICATION_SUBSCRIPTION_EXPLORE)) {
                if(!THPConstants.sISMAIN_ACTIVITY_LAUNCHED) {
                    finish();
                    IntentUtil.openMainTabPage(this);
                    return;
                }
            }
        }
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(this, "THPUserProfileActivity Screen", THPUserProfileActivity.class.getSimpleName());
    }
}
