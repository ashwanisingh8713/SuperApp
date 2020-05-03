package com.netoperation.config.download;

import android.content.Context;
import android.util.Log;

import com.netoperation.config.model.TabsBean;
import com.netoperation.default_db.TableConfiguration;

import io.reactivex.disposables.CompositeDisposable;

public class IconDownload {

    private final CompositeDisposable mDisposable;
    private TableConfiguration mConfiguration;
    private Context mContext;

    public IconDownload(TableConfiguration configuration, Context context) {
        this.mConfiguration = configuration;
        mDisposable = new CompositeDisposable();
        mContext = context;
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

                    }, throwable -> {
                        Log.i("RxDownloader", "Tab Light Failed :: " + tabsBean.getIconUrl().getUrlLight());
                    }));

            // Dark Tab Icon
            mDisposable.add(rxDownloader.downloadInFilesDir(tabsBean.getIconUrl().getUrlDark(),
                    tabsBean.getTitle(), FileUtils.TAB_ICONs_DARK,
                    RxDownloader.DEFAULT_MIME_TYPE, false)
                    .subscribe(filePath -> {

                    }, throwable -> {
                        Log.i("RxDownloader", "Tab Dark Failed :: " + tabsBean.getIconUrl().getUrlLight());
                    }));

        }


        // Other Icons Download Urls

        // Logo Light Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getLight().getLogo(),
                "Logo", FileUtils.LOGOs_LIGHT,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                }, throwable -> {
                    Log.i("RxDownloader", "Logo Light Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getLight().getLogo());
                }));

        // Logo Dark Icon
        mDisposable.add(rxDownloader.downloadInFilesDir(mConfiguration.getOtherIconsDownloadUrls().getDark().getLogo(),
                "Logo", FileUtils.LOGOs_DARK,
                RxDownloader.DEFAULT_MIME_TYPE, false)
                .subscribe(filePath -> {

                }, throwable -> {
                    Log.i("RxDownloader", "Logo Dark Failed :: " + mConfiguration.getOtherIconsDownloadUrls().getDark().getLogo());
                }));



    }


}
