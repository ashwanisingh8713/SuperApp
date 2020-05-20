package com.ns.utils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.netoperation.model.UserProfile;
import com.netoperation.net.ApiManager;
import com.ns.alerts.Alerts;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class SocialLoginUtil {

    private SocialLoginCallbacks mSocialLoginCallbacks;

    //a constant for detecting the login intent result
    public static final int RC_SIGN_IN = 234;

    private FragmentActivity mActivity;

    private String mFrom;

    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;

    // FirebaseAuth mAuth;
    GoogleSignInAccount alreadyloggedAccount;

    // facebook CallbackManager
    private CallbackManager callbackManager;

    private TwitterAuthClient twitterAuthClient;

//    private ProgressDialog mProgressDialog;
    private Fragment mFragment;

    String noEmailTitle = "Login Failed (Email Required)";

    private int delayTimeSec = 6;

    public SocialLoginUtil(FragmentActivity context, String from, ProgressDialog progressDialog, SocialLoginCallbacks socialLoginCallbacks) {
        mActivity = context;
        mFrom = from;
//        mProgressDialog = progressDialog;
        setSocialLoginCallbacks(socialLoginCallbacks);
        if(mSocialLoginCallbacks != null) {
            mSocialLoginCallbacks.onStartSocialLogin();
        }

        if(from != null && from.equalsIgnoreCase("google")) {
            initGoogleSignInProcess();
        } else if(from != null && from.equalsIgnoreCase("facebook")) {
            initFacebookSignInProcess();
        } else if(from != null && from.equalsIgnoreCase("twitter")) {

        }
    }

    public SocialLoginUtil(FragmentActivity context, Fragment fragment, String from, ProgressDialog progressDialog, SocialLoginCallbacks socialLoginCallbacks) {
        mActivity = context;
        mFrom = from;
        mFragment = fragment;
//        mProgressDialog = progressDialog;
        setSocialLoginCallbacks(socialLoginCallbacks);
        if(mSocialLoginCallbacks != null) {
            mSocialLoginCallbacks.onStartSocialLogin();
        }

        if(from != null && from.equalsIgnoreCase("google")) {
            initGoogleSignInProcess();
        } else if(from != null && from.equalsIgnoreCase("facebook")) {
            initFacebookSignInProcess();
        } else if(from != null && from.equalsIgnoreCase("twitter")) {
            initTwitterSignInProcess();
        }
    }

    //Start Google Sign In methods

    private void initGoogleSignInProcess() {
        String id = mActivity.getString(R.string.custom_web_client_id);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mActivity.getString(R.string.default_web_client_id))
//                .requestIdToken(id)
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(mActivity, gso);

        alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(mActivity);

        if(alreadyloggedAccount != null) {
            Alerts.showToast(mActivity, "Already Logged In with Gmail");
            googleSignIn();
        } else {
            googleSignIn();
        }

    }

    private void googleSignIn() {
        if(mSocialLoginCallbacks != null) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            mSocialLoginCallbacks.initGoogleLogin(signInIntent);
        }
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        getGmailLoginDetails(acct);
    }

    private void getGmailLoginDetails(GoogleSignInAccount acct) {
        String provider="Google";
        String socialId=acct.getId();
        String userEmail=acct.getEmail();
        String userName=acct.getDisplayName();
        ApiManager.socialLogin(mActivity, ResUtil.getDeviceId(mActivity), BuildConfig.ORIGIN_URL, provider, socialId, userEmail, userName, BuildConfig.SITEID)
                .delay(delayTimeSec, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(keyValueModel -> {
                            if (mActivity == null) {
                                return;
                            }

                            String userId = keyValueModel.getUserId();
                            final boolean isNewAccount = keyValueModel.isNewAccount();

                            if (TextUtils.isEmpty(userId)) {
                                //Clear Gmail Session
                                if(mGoogleSignInClient != null) {
                                    GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(mActivity);
                                    if(googleAccount!=null) {
                                        mGoogleSignInClient.signOut()
                                                .addOnCompleteListener(mActivity, task -> {
                                                    //Signed out
                                                    alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(mActivity);
                                                });
                                    }
                                }
                                Alerts.showAlertDialogOKBtn(mActivity, "", keyValueModel.getName());
                            } else {
                                // Making server request to get User Info
                                ApiManager.getUserInfoObject(mActivity, keyValueModel.getToken(), BuildConfig.SITEID, ResUtil.getDeviceId(mActivity), userId, "google", "google")
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(userProfile->{

                                            if (mActivity == null) {
                                                return;
                                            }

                                            if (userProfile != null && userProfile.getUserId() != null
                                                    && !ResUtil.isEmpty(userProfile.getUserId())) {
                                                // TODO, process for user sign - In
                                                if(mSocialLoginCallbacks != null) {
                                                    mSocialLoginCallbacks.onFinishSocialLogin(true, userProfile, "google", isNewAccount);
                                                }
                                            } else {
                                                Alerts.showErrorDailog(mActivity.getSupportFragmentManager(), "Sorry",
                                                        "We are fetching some technical problem.\n Please try later.");
                                            }

                                        }, throwable -> {
                                            if(mActivity != null) {
                                                if(mSocialLoginCallbacks != null) {
                                                    mSocialLoginCallbacks.onErrorSocialLogin();
                                                }
                                                Alerts.showSnackbar(mActivity, mActivity.getResources().getString(R.string.something_went_wrong));
                                            }
                                        }, () ->{
                                            if(mActivity != null) {
                                                if(mSocialLoginCallbacks != null) {
                                                    mSocialLoginCallbacks.onErrorSocialLogin();
                                                }
                                            }
                                        });


                            }
                        }, throwable -> {
                            if(mActivity != null) {
                                if(mSocialLoginCallbacks != null) {
                                    mSocialLoginCallbacks.onErrorSocialLogin();
                                }
                                Alerts.showSnackbar(mActivity, mActivity.getResources().getString(R.string.something_went_wrong));
                            }
                        },
                        () -> {

                        });

    }

    //End Google Sign In methods


    //Start Facebook Sign In methods

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    private void initFacebookSignInProcess() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(mFragment, Arrays.asList("email","public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request=GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject json, GraphResponse response) {
                                if (response.getError() != null) {
                                    Alerts.showToast(mActivity, "Error");
                                    AccessToken.setCurrentAccessToken(null);
                                    if(mSocialLoginCallbacks != null) {
                                        mSocialLoginCallbacks.onErrorSocialLogin();
                                    }
                                } else {
                                    getFaceBookLoginDetails(json);
                                }
                            }
                        });
                        Bundle parameters=new Bundle();
                        parameters.putString("fields", "name, email, id");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        if(mSocialLoginCallbacks != null) {
                            mSocialLoginCallbacks.onErrorSocialLogin();
                        }
                        Alerts.showToast(mActivity, "Facebook Login Cancelled");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        if(mSocialLoginCallbacks != null) {
                            mSocialLoginCallbacks.onErrorSocialLogin();
                        }
                        Alerts.showToast(mActivity, "Error Occurred while Sign In");
                    }
                });
    }


    private void getFaceBookLoginDetails(JSONObject json) {
        String provider;
        String userEmail;
        String socialId;
        String userName;
        try {
            provider="Facebook";
            socialId=json.getString("id");
            userName=json.getString("name");
            if(json.has("email")){
                if(json.getString("email")==null || json.get("email").equals("")) {
                    if(mSocialLoginCallbacks != null) {
                        mSocialLoginCallbacks.onErrorSocialLogin();
                    }
                    Alerts.showAlertDialogOKBtn(mActivity, noEmailTitle, " We didn't find your primary contact details, please make it is visible to create the account from social Login");
                }else{
                    userEmail=json.getString("email");
                    ApiManager.socialLogin(mActivity, ResUtil.getDeviceId(mActivity), BuildConfig.ORIGIN_URL, provider, socialId, userEmail, userName, BuildConfig.SITEID)
                            .delay(delayTimeSec, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(keyValueModel -> {
                                        if (mActivity == null) {
                                            return;
                                        }

                                        String userId = keyValueModel.getUserId();
                                        final boolean isNewAccount = keyValueModel.isNewAccount();

                                        if (TextUtils.isEmpty(userId)) {
                                            // Facebook Clear Session
                                            if(AccessToken.getCurrentAccessToken() != null) {
                                                AccessToken.setCurrentAccessToken(null);
                                            }
                                            Alerts.showAlertDialogOKBtn(mActivity, "", keyValueModel.getName());
                                        } else {
                                            // Making server request to get User Info
                                            ApiManager.getUserInfoObject(mActivity, keyValueModel.getToken(), BuildConfig.SITEID, ResUtil.getDeviceId(mActivity), userId, "facebook", "facebook")
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(userProfile->{

                                                        if (mActivity == null) {
                                                            return;
                                                        }

                                                        if (userProfile != null && userProfile.getUserId() != null
                                                                && !ResUtil.isEmpty(userProfile.getUserId())) {
                                                            // TODO, process for user sign - In
                                                            if(mSocialLoginCallbacks != null) {
                                                                mSocialLoginCallbacks.onFinishSocialLogin(true, userProfile, "facebook", isNewAccount);
                                                            }
                                                        } else {
                                                            Alerts.showErrorDailog(mActivity.getSupportFragmentManager(), "Sorry",
                                                                    "We are fetching some technical problem.\n Please try later.");
                                                        }


                                                    }, throwable -> {
                                                        if(mActivity != null) {
                                                            if(mSocialLoginCallbacks != null) {
                                                                mSocialLoginCallbacks.onErrorSocialLogin();
                                                            }
                                                            Alerts.showSnackbar(mActivity, mActivity.getResources().getString(R.string.something_went_wrong));
                                                        }
                                                    }, () ->{
                                                        if(mActivity != null) {
                                                            if(mSocialLoginCallbacks != null) {
                                                                mSocialLoginCallbacks.onErrorSocialLogin();
                                                            }
                                                        }
                                                    });


                                        }
                                    }, throwable -> {
                                        if(mActivity != null) {
                                            if(mSocialLoginCallbacks != null) {
                                                mSocialLoginCallbacks.onErrorSocialLogin();
                                            }
                                            Alerts.showSnackbar(mActivity, mActivity.getResources().getString(R.string.something_went_wrong));
                                        }
                                    },
                                    () -> {

                                    });
                }
            }else{
                Alerts.showAlertDialogOKBtn(mActivity, "Sorry !", " We didn't find your primary contact details, please make it is visible to create the account from social Login");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Alerts.showToast(mActivity, "Error while getting the values");
        }
    }

    /*End Facebook Sign In method */

    /* Start Twitter SignIn method */

    private void configureTwitter() {
        TwitterConfig config = new TwitterConfig.Builder(mActivity)
                .logger(new DefaultLogger(Log.DEBUG))//enable logging when app is in debug mode
                .twitterAuthConfig(new TwitterAuthConfig(BuildConfig.TWITTER_API_KEY, BuildConfig.TWITTER_API_SECRET_KEY))//pass the created app Consumer KEY and Secret also called API Key and Secret
                .debug(true)//enable debug mode
                .build();

        //finally initialize twitter with created configs
        Twitter.initialize(config);
    }

    private void initTwitterSignInProcess() {

        configureTwitter();

        //initialize twitter auth twitterAuthClient
        twitterAuthClient = new TwitterAuthClient();

        if (getTwitterSession() == null) {

            //if user is not authenticated start authenticating
            twitterAuthClient.authorize(mActivity, new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {

                    // Do something with result, which provides a TwitterSession for making API calls
                    TwitterSession twitterSession = result.data;

                    //Call fetch email only when permission is granted
                    fetchTwitterEmail(twitterSession);
                }

                @Override
                public void failure(TwitterException e) {
                    if(mSocialLoginCallbacks !=null) {
                        mSocialLoginCallbacks.onErrorSocialLogin();
                    }
                    // Do something on failure
                    Alerts.showToast(mActivity, "Failed to authenticate. Please try again.");
                }
            });
        } else {
            //if user is already authenticated direct call fetch twitter email api
            Alerts.showToast(mActivity, "User already authenticated");
            fetchTwitterEmail(getTwitterSession());
        }
    }

    public void fetchTwitterEmail(final TwitterSession twitterSession) {
        twitterAuthClient.requestEmail(twitterSession, new Callback<String>() {
            @Override
            public void success(Result<String> result) {
                //here it will give u only email and rest of other information u can get from TwitterSession
                //                // Do something with result, which provides a TwitterSession for making API calls
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();

                if(!ResUtil.isEmpty(result.data) && ResUtil.isValidEmail(result.data)) {
                    //Alerts.showToast(getActivity(), "Logged in Successfully"); //Duplicate alert
                    getTwitterLoginDetails(result, session);
                }
                else {
                    if(mSocialLoginCallbacks !=null) {
                        mSocialLoginCallbacks.onErrorSocialLogin();
                    }
                    Alerts.showAlertDialogOKBtn(mActivity, noEmailTitle,
                            "Please check whether you have updated your email address in twitter?");
                    try {
                        // Twitter Logout
                        if (TwitterCore.getInstance().getSessionManager().getActiveSession() != null) {
                            CookieSyncManager.createInstance(mActivity);
                            CookieManager cookieManager = CookieManager.getInstance();
                            cookieManager.removeSessionCookie();
                            TwitterCore.getInstance().getSessionManager().clearActiveSession();
                        }
                    } catch (Exception ignore){
                        Log.i("", "");
                        if(mSocialLoginCallbacks !=null) {
                            mSocialLoginCallbacks.onErrorSocialLogin();
                        }
                    }

                }

            }

            @Override
            public void failure(TwitterException exception) {
                if(mSocialLoginCallbacks !=null) {
                    mSocialLoginCallbacks.onErrorSocialLogin();
                }
                Alerts.showToast(mActivity, "Failed to authenticate. Please try again.");

            }
        });
    }

    public TwitterAuthClient getTwitterAuthClient() {
        return twitterAuthClient;
    }

    private void getTwitterLoginDetails(Result<String> result, TwitterSession session) {
        String provider="Twitter";
        String socialId=String.valueOf(session.getId());
        String userEmail=result.data;
        String userName=session.getUserName();
        ApiManager.socialLogin(mActivity, ResUtil.getDeviceId(mActivity), BuildConfig.ORIGIN_URL, provider, socialId, userEmail, userName, BuildConfig.SITEID)
                .delay(delayTimeSec, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(keyValueModel -> {
                            if (mActivity == null) {
                                return;
                            }

                            String userId = keyValueModel.getUserId();
                            final boolean isNewAccount = keyValueModel.isNewAccount();

                            if (ResUtil.isEmpty(userId)) {
                                if(mSocialLoginCallbacks !=null) {
                                    mSocialLoginCallbacks.onErrorSocialLogin();
                                }

                                //Clear Twitter Session
                                try {
                                    // Twitter Logout
                                    if (TwitterCore.getInstance().getSessionManager().getActiveSession() != null) {
                                        CookieSyncManager.createInstance(mActivity);
                                        CookieManager cookieManager = CookieManager.getInstance();
                                        cookieManager.removeSessionCookie();
                                        TwitterCore.getInstance().getSessionManager().clearActiveSession();
                                    }
                                } catch (Exception ignore){
                                    Log.i("", "");
                                }
                                Alerts.showAlertDialogOKBtn(mActivity, "", keyValueModel.getName());
                            } else {
                                // Making server request to get User Info
                                ApiManager.getUserInfoObject(mActivity, keyValueModel.getToken(), BuildConfig.SITEID, ResUtil.getDeviceId(mActivity), userId, "twitter", "twitter")
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(userProfile->{

                                            if (mActivity == null) {
                                                return;
                                            }

                                            if (userProfile != null && userProfile.getUserId() != null
                                                    && !ResUtil.isEmpty(userProfile.getUserId())) {
                                                // TODO, process for user sign - In
                                                if(mSocialLoginCallbacks !=null) {
                                                    mSocialLoginCallbacks.onFinishSocialLogin(true, userProfile, "twitter", isNewAccount);
                                                }
                                            } else {
                                                if(mSocialLoginCallbacks !=null) {
                                                    mSocialLoginCallbacks.onErrorSocialLogin();
                                                }
                                                Alerts.showErrorDailog(mActivity.getSupportFragmentManager(), "Sorry",
                                                        "We are fetching some technical problem.\n Please try later.");
                                            }


                                        }, throwable -> {
                                            if(mActivity != null) {
                                                if(mSocialLoginCallbacks !=null) {
                                                    mSocialLoginCallbacks.onErrorSocialLogin();
                                                }
                                                Alerts.showSnackbar(mActivity, mActivity.getResources().getString(R.string.something_went_wrong));
                                            }
                                        }, () ->{
                                            if(mActivity != null) {
                                                if(mSocialLoginCallbacks !=null) {
                                                    mSocialLoginCallbacks.onErrorSocialLogin();
                                                }
                                            }
                                        });


                            }
                        }, throwable -> {
                            if(mActivity != null) {
                                if(mSocialLoginCallbacks !=null) {
                                    mSocialLoginCallbacks.onErrorSocialLogin();
                                }

                                Alerts.showSnackbar(mActivity, mActivity.getResources().getString(R.string.something_went_wrong));
                            }
                        },
                        () -> {

                        });
    }

    private TwitterSession getTwitterSession() {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();

        //NOTE : if you want to get token and secret too use uncomment the below code

       /* TwitterAuthToken authToken = session.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;*/

        return session;
    }
    /* End Twitter SignIn Method*/


    // Callbacks for social login
    private void setSocialLoginCallbacks(SocialLoginCallbacks socialLoginCallbacks) {
        mSocialLoginCallbacks = socialLoginCallbacks;
    }

    public interface SocialLoginCallbacks {
        void onStartSocialLogin();
        void onFinishSocialLogin(boolean isKillToBecomeMemberActivity, UserProfile userProfile, String from, boolean isNewAccount);
        void onErrorSocialLogin();
        void initGoogleLogin(Intent signInIntent);
        void initFacebookLogin(Intent signInIntent);
    }


}
