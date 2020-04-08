package com.ns.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.netoperation.model.RecoBean;
import com.netoperation.net.ApiManager;
import com.netoperation.util.NetConstants;
import com.ns.alerts.Alerts;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ashwanisingh on 11/10/18.
 */

public class WebViewLinkClick {


    public WebViewLinkClick() {

    }

    public void linkClick(WebView webView, Context context, String articleAid) {
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //javascript support
        webView.getSettings().setJavaScriptEnabled(true);
        //html5 support
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setLoadWithOverviewMode(true);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //Clicked url
                //if u use super() it will load url into webview
                URI uri = null;
                try {
                    uri = new URI(url);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                String domain = uri.getHost();
                String path = uri.getPath();

                String aid = ""+CommonUtil.getArticleIdFromArticleUrl(url);

                if (domain != null && domain.equalsIgnoreCase("vuukle.com")) {
                    String[] uris = url.split("&uri=");
                    if (uris.length == 2) {
                        url = uris[1];
                    }
                }

                openActivity(context,  aid, url);

                return true;
            }
        });
    }


    private void openActivity(Context context, String aid, String url) {
        final ProgressDialog progress = Alerts.showProgressDialog(context);
        ApiManager.isExistInAllArticle(context, aid)
                .subscribeOn(Schedulers.newThread())
                .delay(400, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                               @Override
                               public void accept(Boolean aBoolean) {
                                   if (aBoolean) {
                                       IntentUtil.openDetailActivity(context, NetConstants.RECO_TEMP_NOT_EXIST, url, 0, aid);
                                       progress.dismiss();
                                   } else {
                                       String SEARCH_BY_ARTICLE_ID_URL = "";
                                       if(BuildConfig.IS_PRODUCTION) {
                                           SEARCH_BY_ARTICLE_ID_URL = BuildConfig.PRODUCTION_SEARCH_BY_ARTICLE_ID_URL;
                                       } else {
                                           SEARCH_BY_ARTICLE_ID_URL = BuildConfig.STATGGING_SEARCH_BY_ARTICLE_ID_URL;
                                       }
                                       // Making Server request to get Article from server
                                       // and Saving into DB, with SectionName = "tempSec"
                                       Observable<RecoBean> observable =  ApiManager.articleDetailFromServer(context, aid, SEARCH_BY_ARTICLE_ID_URL, NetConstants.RECO_TEMP_NOT_EXIST);
                                       observable.observeOn(AndroidSchedulers.mainThread())
                                               .subscribe(new Consumer<RecoBean>() {
                                                              @Override
                                                              public void accept(RecoBean recoBean)  {
                                                                  if(context == null) {
                                                                      return;
                                                                  }
                                                                  if(recoBean != null &&  recoBean.getArticleId() != null && !ResUtil.isEmpty(recoBean.getArticleId())) {
                                                                      IntentUtil.openDetailActivity(context, NetConstants.RECO_TEMP_NOT_EXIST, url, 0, recoBean.getArticleId());
                                                                  }
                                                                  else {
                                                                      // Opening Article In Web Page
                                                                      IntentUtil.openWebActivity(context, aid, url);
                                                                  }

                                                                  if(progress != null && context != null) {
                                                                      progress.dismiss();
                                                                  }
                                                              }
                                                          },
                                                       new Consumer<Throwable>() {
                                                           @Override
                                                           public void accept(Throwable throwable)  {
                                                               Log.i("", "");
                                                               if(progress != null && context != null) {
                                                                   progress.dismiss();
                                                               }
                                                               if(context != null && context instanceof Activity) {
                                                                   Alerts.showSnackbar((Activity) context, context.getResources().getString(R.string.something_went_wrong));
                                                               }
                                                           }
                                                       });



                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                progress.dismiss();
                            }
                        },
                        new Action() {
                            @Override
                            public void run() {

                            }
                        });
    }




}
