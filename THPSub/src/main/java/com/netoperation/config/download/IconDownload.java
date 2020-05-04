package com.netoperation.config.download;

import android.content.Context;
import android.util.Log;

import com.netoperation.config.model.TabsBean;
import com.netoperation.default_db.TableConfiguration;

import io.reactivex.disposables.CompositeDisposable;

public class IconDownload {

    public interface DownloadStatusCallback {
        void success(String filePath);
        void fail(String url);
    }

    private final CompositeDisposable mDisposable;
    private TableConfiguration mConfiguration;
    private Context mContext;
    private DownloadStatusCallback mDownloadStatusCallback;

    public IconDownload(TableConfiguration configuration, Context context, DownloadStatusCallback downloadStatusCallback) {
        this.mConfiguration = configuration;
        mDisposable = new CompositeDisposable();
        mContext = context;
        mDownloadStatusCallback = downloadStatusCallback;
    }

    public void startDownloading() {

        RxDownloader rxDownloader = new RxDownloader(mContext);


        // Tab Icons Downloading
        for (TabsBean tabsBean : mConfiguration.getTabs()) {
            // Light Tab Icon
            mDisposable.add(rxDownloader.downloadInFilesDir(tabsBean.getIconUrl().getUrlLight(),
                    tabsBean.getTitle(), FileUtils.TAB_ICONs_LIGHT,
                    RxDownloader.DEFAULT_MIME_TYPE, false)
                    .subscribe(filePath -> {
                            if(mDownloadStatusCallback != null) {
                                mDownloadStatusCallback.success(filePath);
                            }
                    }, throwable -> {
                        if(mDownloadStatusCallback != null) {
                            mDownloadStatusCallback.fail(tabsBean.getIconUrl().getUrlLight());
                        }
                        Log.i("RxDownloader", "Tab Light Failed :: " + tabsBean.getIconUrl().getUrlLight());
                    }));

            // Dark Tab Icon
            mDisposable.add(rxDownloader.downloadInFilesDir(tabsBean.getIconUrl().getUrlDark(),
                    tabsBean.getTitle(), FileUtils.TAB_ICONs_DARK,
                    RxDownloader.DEFAULT_MIME_TYPE, false)
                    .subscribe(filePath -> {
                        if(mDownloadStatusCallback != null) {
                            mDownloadStatusCallback.success(filePath);
                        }
                    }, throwable -> {
                        if(mDownloadStatusCallback != null) {
                            mDownloadStatusCallback.fail(tabsBean.getIconUrl().getUrlLight());
                        }
                        Log.i("RxDownloader", "Tab Dark Failed :: " + tabsBean.getIconUrl().getUrlLight());
                    }));

        }


        // Other Icons Download Urls

        // Logo Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getLogo(),
                "Logo", FileUtils.LOGOs_LIGHT,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getLogo());
                    }
                    Log.i("RxDownloader", "Logo Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getLogo());
                }));


        ///////// Start Listing Light Icons //////////

        // Bookmark Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getListing().getBookmark(),
                "bookmark", FileUtils.LISTING_ICONs_LIGHT,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getListing().getBookmark());
                    }
                    Log.i("RxDownloader", "Bookmark Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getListing().getBookmark());
                }));

        // UnBookmark Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getListing().getUnbookmark(),
                "unBookmark", FileUtils.LISTING_ICONs_LIGHT,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getListing().getUnbookmark());
                    }
                    Log.i("RxDownloader", "UnBookmark Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getListing().getUnbookmark());
                }));

        // Like Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getListing().getLike(),
                "like", FileUtils.LISTING_ICONs_LIGHT,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getListing().getLike());
                    }
                    Log.i("RxDownloader", "Like Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getListing().getLike());
                }));

        // Dislike Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getListing().getDislike(),
                "disLike", FileUtils.LISTING_ICONs_LIGHT,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getListing().getDislike());
                    }
                    Log.i("RxDownloader", "Dis-Like Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getListing().getDislike());
                }));

        // Favorite Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getListing().getFavorite(),
                "favorite", FileUtils.LISTING_ICONs_LIGHT,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getListing().getFavorite());
                    }
                    Log.i("RxDownloader", "Favorite Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getListing().getFavorite());
                }));

        // Unfavorite Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getListing().getUnfavorite(),
                "unfavorite", FileUtils.LISTING_ICONs_LIGHT,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getListing().getUnfavorite());
                    }
                    Log.i("RxDownloader", "Unfavorite Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getListing().getUnfavorite());
                }));

        ///////// End Listing Light Icons //////////

        ///////// Start Topbar Light Icons //////////

        // Left_slider_three_line Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getLeft_slider_three_line(),
                "Left_slider_three_line", FileUtils.LISTING_ICONs_LIGHT,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getLeft_slider_three_line());
                    }
                    Log.i("RxDownloader", "Top bar Left_slider_three_line Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getLeft_slider_three_line());
                }));

        // logo Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getLogo(),
                "logo", FileUtils.LISTING_ICONs_LIGHT,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getLogo());
                    }
                    Log.i("RxDownloader", "Topbar Logo Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getLogo());
                }));

        // search Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getSearch(),
                "search", FileUtils.TOPBAR_ICONs_LIGHT,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getSearch());
                    }
                    Log.i("RxDownloader", "Topbar search Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getSearch());
                }));

        // overflow_verticle_dot Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getOverflow_verticle_dot(),
                "overflow_verticle_dot", FileUtils.TOPBAR_ICONs_LIGHT,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getOverflow_verticle_dot());
                    }
                    Log.i("RxDownloader", "Topbar overflow_verticle_dot Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getOverflow_verticle_dot());
                }));


        // back Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getBack(),
                "unfavorite", FileUtils.TOPBAR_ICONs_LIGHT,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getBack());
                    }
                    Log.i("RxDownloader", "Topbar back Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getBack());
                }));


        // ttsPlay Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getTtsPlay(),
                "ttsPlay", FileUtils.TOPBAR_ICONs_LIGHT,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getTtsPlay());
                    }
                    Log.i("RxDownloader", "Topbar ttsPlay Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getTtsPlay());
                }));

        // ttsPause Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getTtsPause(),
                "ttsPause", FileUtils.TOPBAR_ICONs_LIGHT,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getTtsPause());
                    }
                    Log.i("RxDownloader", "Topbar ttsPause Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getTtsPause());
                }));

        // comment Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getComment(),
                "comment", FileUtils.TOPBAR_ICONs_LIGHT,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getComment());
                    }
                    Log.i("RxDownloader", "Topbar back Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getComment());
                }));

        // bookmark Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getBookmark(),
                "bookmark", FileUtils.TOPBAR_ICONs_LIGHT,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getBookmark());
                    }
                    Log.i("RxDownloader", "Topbar bookmark Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getBookmark());
                }));

        // unbookmark Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getUnbookmark(),
                "unbookmark", FileUtils.TOPBAR_ICONs_LIGHT,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getUnbookmark());
                    }
                    Log.i("RxDownloader", "Topbar unbookmark Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getUnbookmark());
                }));

        // textSize Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getTextSize(),
                "textSize", FileUtils.TOPBAR_ICONs_LIGHT,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getTextSize());
                    }
                    Log.i("RxDownloader", "Topbar textSize Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getTextSize());
                }));

        // share Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getShare(),
                "share", FileUtils.TOPBAR_ICONs_LIGHT,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getShare());
                    }
                    Log.i("RxDownloader", "Topbar share Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getShare());
                }));

        // crown Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getCrown(),
                "crown", FileUtils.TOPBAR_ICONs_LIGHT,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getCrown());
                    }
                    Log.i("RxDownloader", "Topbar crown Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getCrown());
                }));

        // like Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getLike(),
                "like", FileUtils.TOPBAR_ICONs_LIGHT,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getLike());
                    }
                    Log.i("RxDownloader", "Topbar like Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getLike());
                }));

        // dislike Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getDislike(),
                "dislike", FileUtils.TOPBAR_ICONs_LIGHT,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getDislike());
                    }
                    Log.i("RxDownloader", "Topbar dislike Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getDislike());
                }));

        // favorite Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getFavorite(),
                "favorite", FileUtils.TOPBAR_ICONs_LIGHT,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getFavorite());
                    }
                    Log.i("RxDownloader", "Topbar favorite Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getFavorite());
                }));

        // unfavorite Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getUnfavorite(),
                "unfavorite", FileUtils.TOPBAR_ICONs_LIGHT,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getUnfavorite());
                    }
                    Log.i("RxDownloader", "Topbar unfavorite Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getUnfavorite());
                }));

        ///////// End Topbar Light Icons //////////

        ///////// Start PlaceHolder Dark Icons //////////

        // banner Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getPlaceHolder().getBanner(),
                "banner", FileUtils.PLACE_HOLDER_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getPlaceHolder().getBanner());
                    }
                    Log.i("RxDownloader", "Topbar banner Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getPlaceHolder().getBanner());
                }));

        // ads Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getPlaceHolder().getAds(),
                "ads", FileUtils.PLACE_HOLDER_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getPlaceHolder().getAds());
                    }
                    Log.i("RxDownloader", "Topbar ads Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getPlaceHolder().getAds());
                }));

        // thumb Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getPlaceHolder().getThumb(),
                "thumb", FileUtils.PLACE_HOLDER_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getPlaceHolder().getThumb());
                    }
                    Log.i("RxDownloader", "Topbar thumb Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getPlaceHolder().getThumb());
                }));

        // widget Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getPlaceHolder().getWidget(),
                "widget", FileUtils.PLACE_HOLDER_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getLight().getPlaceHolder().getWidget());
                    }
                    Log.i("RxDownloader", "Topbar widget Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getPlaceHolder().getWidget());
                }));

        ///////// End PlaceHolder Dark Icons //////////

        ////////////////////////////////////////////////////////////////////////////////////////////

                // ############## Start Dark Icons

        ////////////////////////////////////////////////////////////////////////////////////////////

        // Logo Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getLogo(),
                "Logo", FileUtils.LOGOs_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getLogo());
                    }
                    Log.i("RxDownloader", "Logo Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getLogo());
                }));

        ///////// Start Listing Dark Icons //////////

        // Bookmark Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getListing().getBookmark(),
                "bookmark", FileUtils.LISTING_ICONs_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getListing().getBookmark());
                    }
                    Log.i("RxDownloader", "Bookmark Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getListing().getBookmark());
                }));

        // UnBookmark Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getListing().getUnbookmark(),
                "unBookmark", FileUtils.LISTING_ICONs_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getListing().getUnbookmark());
                    }
                    Log.i("RxDownloader", "UnBookmark Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getListing().getUnbookmark());
                }));

        // Like Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getListing().getLike(),
                "like", FileUtils.LISTING_ICONs_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getListing().getLike());
                    }
                    Log.i("RxDownloader", "Like Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getListing().getLike());
                }));

        // Dislike Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getListing().getDislike(),
                "disLike", FileUtils.LISTING_ICONs_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getListing().getDislike());
                    }
                    Log.i("RxDownloader", "Dis-Like Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getListing().getDislike());
                }));

        // Favorite Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getListing().getFavorite(),
                "favorite", FileUtils.LISTING_ICONs_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getListing().getFavorite());
                    }
                    Log.i("RxDownloader", "Favorite Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getListing().getFavorite());
                }));

        // Unfavorite Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getListing().getUnfavorite(),
                "unfavorite", FileUtils.LISTING_ICONs_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getListing().getUnfavorite());
                    }
                    Log.i("RxDownloader", "Unfavorite Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getListing().getUnfavorite());
                }));

        ///////// End Listing Dark Icons //////////

        ///////// Start Topbar Dark Icons //////////

        // Left_slider_three_line Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getLeft_slider_three_line(),
                "Left_slider_three_line", FileUtils.TOPBAR_ICONs_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getLeft_slider_three_line());
                    }
                    Log.i("RxDownloader", "Top bar Left_slider_three_line Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getLeft_slider_three_line());
                }));

        // logo Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getLogo(),
                "logo", FileUtils.TOPBAR_ICONs_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getLogo());
                    }
                    Log.i("RxDownloader", "Topbar Logo Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getLogo());
                }));

        // search Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getSearch(),
                "search", FileUtils.TOPBAR_ICONs_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getSearch());
                    }
                    Log.i("RxDownloader", "Topbar search Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getSearch());
                }));

        // overflow_verticle_dot Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getOverflow_verticle_dot(),
                "overflow_verticle_dot", FileUtils.TOPBAR_ICONs_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getOverflow_verticle_dot());
                    }
                    Log.i("RxDownloader", "Topbar overflow_verticle_dot Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getOverflow_verticle_dot());
                }));


        // back Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getBack(),
                "unfavorite", FileUtils.TOPBAR_ICONs_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getBack());
                    }
                    Log.i("RxDownloader", "Topbar back Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getBack());
                }));


        // ttsPlay Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getTtsPlay(),
                "ttsPlay", FileUtils.TOPBAR_ICONs_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getTtsPlay());
                    }
                    Log.i("RxDownloader", "Topbar ttsPlay Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getTtsPlay());
                }));

        // ttsPause Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getTtsPause(),
                "ttsPause", FileUtils.TOPBAR_ICONs_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getTtsPause());
                    }
                    Log.i("RxDownloader", "Topbar ttsPause Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getTtsPause());
                }));

        // comment Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getComment(),
                "comment", FileUtils.TOPBAR_ICONs_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getComment());
                    }
                    Log.i("RxDownloader", "Topbar back Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getComment());
                }));

        // bookmark Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getBookmark(),
                "bookmark", FileUtils.TOPBAR_ICONs_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getBookmark());
                    }
                    Log.i("RxDownloader", "Topbar bookmark Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getBookmark());
                }));

        // unbookmark Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getUnbookmark(),
                "unbookmark", FileUtils.TOPBAR_ICONs_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getUnbookmark());
                    }
                    Log.i("RxDownloader", "Topbar unbookmark Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getUnbookmark());
                }));

        // textSize Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getTextSize(),
                "textSize", FileUtils.TOPBAR_ICONs_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getTextSize());
                    }
                    Log.i("RxDownloader", "Topbar textSize Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getTextSize());
                }));

        // share Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getShare(),
                "share", FileUtils.TOPBAR_ICONs_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getShare());
                    }
                    Log.i("RxDownloader", "Topbar share Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getShare());
                }));

        // crown Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar().getCrown(),
                "crown", FileUtils.TOPBAR_ICONs_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getCrown());
                    }
                    Log.i("RxDownloader", "Topbar crown Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getCrown());
                }));

        // like Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getLike(),
                "like", FileUtils.TOPBAR_ICONs_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getLike());
                    }
                    Log.i("RxDownloader", "Topbar like Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getLike());
                }));

        // dislike Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getDislike(),
                "dislike", FileUtils.TOPBAR_ICONs_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getDislike());
                    }
                    Log.i("RxDownloader", "Topbar dislike Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getDislike());
                }));

        // favorite Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getFavorite(),
                "favorite", FileUtils.TOPBAR_ICONs_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getFavorite());
                    }
                    Log.i("RxDownloader", "Topbar favorite Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getFavorite());
                }));

        // unfavorite Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getUnfavorite(),
                "unfavorite", FileUtils.TOPBAR_ICONs_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getUnfavorite());
                    }
                    Log.i("RxDownloader", "Topbar unfavorite Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar().getUnfavorite());
                }));

        ///////// End Topbar Dark Icons //////////


        ///////// Start PlaceHolder Dark Icons //////////

        // banner Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getPlaceHolder().getBanner(),
                "banner", FileUtils.PLACE_HOLDER_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getPlaceHolder().getBanner());
                    }
                    Log.i("RxDownloader", "Topbar banner Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getPlaceHolder().getBanner());
                }));

        // ads Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getPlaceHolder().getAds(),
                "ads", FileUtils.PLACE_HOLDER_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getPlaceHolder().getAds());
                    }
                    Log.i("RxDownloader", "Topbar ads Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getPlaceHolder().getAds());
                }));

        // thumb Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getPlaceHolder().getThumb(),
                "thumb", FileUtils.PLACE_HOLDER_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getPlaceHolder().getThumb());
                    }
                    Log.i("RxDownloader", "Topbar thumb Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getPlaceHolder().getThumb());
                }));
        // widget Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getPlaceHolder().getWidget(),
                "widget", FileUtils.PLACE_HOLDER_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.success(filePath);
                    }
                }, throwable -> {
                    if(mDownloadStatusCallback != null) {
                        mDownloadStatusCallback.fail(mConfiguration.getOtherIconsDownloadUrls().getDark().getPlaceHolder().getWidget());
                    }
                    Log.i("RxDownloader", "Topbar widget Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getPlaceHolder().getWidget());
                }));

        ///////// End PlaceHolder Dark Icons //////////

        ////////////////////////////////////////////////////////////////////////////////////////////

        // ############## End Dark Icons

        ////////////////////////////////////////////////////////////////////////////////////////////


    }


}
