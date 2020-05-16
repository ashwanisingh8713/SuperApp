package com.netoperation.config.download;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.webkit.URLUtil;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.netoperation.config.model.ListingIconUrl;
import com.netoperation.config.model.OtherIconUrls;
import com.netoperation.config.model.PlaceHolder;
import com.netoperation.config.model.TabsBean;
import com.netoperation.config.model.TopbarIconUrl;
import com.netoperation.db.THPDB;
import com.netoperation.util.DefaultPref;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;


public class IconDownloadService extends Service {

    //private NotificationCompat.Builder notificationBuilder;
    //private NotificationManager notificationManager;
    private int totalFileSize;

    protected final CompositeDisposable mDisposable = new CompositeDisposable();

    private IconRetroInterface retrofitInterface;

    public static final String MESSAGE_STATUS = "MESSAGE_STATUS";
    public static final String MESSAGE_PROGRESS = "MESSAGE_PROGRESS";
    public static final String MESSAGE_FAILED = "MESSAGE_FAILED";
    public static final String MESSAGE_SUCCESS = "MESSAGE_SUCCESS";
    public static final String MESSAGE_REQUEST_SENT = "MESSAGE_REQUEST_SENT";

    public static final String STATE = "STATE";
    public static final String STATE_CHECK = "STATE_CHECK";
    public static final String STATE_START = "STATE_START";

    public static final int STATUS_NOT_RUNNING = 0;
    public static final int STATUS_RUNNING = 1;

    private int mStatus = STATUS_NOT_RUNNING;
    private int mRequestCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        /*notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_download)
                .setContentTitle("Download")
                .setContentText("Downloading File")
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());*/

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultPref.getInstance(this).getDefaultContentBaseUrl())
                .build();

        retrofitInterface = retrofit.create(IconRetroInterface.class);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String state = intent.getExtras().getString(STATE);
        if(state.equals(STATE_CHECK)) {
            sendStatusNotification();
        }
        else if(state.equals(STATE_START)) {
            mStatus = STATUS_RUNNING;
            mDisposable.add(THPDB.getInstance(this).daoConfiguration().getConfigurationSingle()
                    .subscribeOn(Schedulers.io())
                    .map(tableConfiguration -> {

                        ArrayList<String> foldersName = new ArrayList<>();
                        foldersName.add(FileUtils.LISTING_ICONs_LIGHT);
                        foldersName.add(FileUtils.LISTING_ICONs_DARK);
                        foldersName.add(FileUtils.PLACE_HOLDER_DARK);
                        foldersName.add(FileUtils.PLACE_HOLDER_LIGHT);
                        foldersName.add(FileUtils.TAB_ICONs_DARK);
                        foldersName.add(FileUtils.TAB_ICONs_LIGHT);
                        foldersName.add(FileUtils.LOGOs_DARK);
                        foldersName.add(FileUtils.LOGOs_LIGHT);
                        foldersName.add(FileUtils.TOPBAR_ICONs_LIGHT);
                        foldersName.add(FileUtils.TOPBAR_ICONs_DARK);

                        // Delete Folders
                        for (String destinationFolder : foldersName) {
                            File folder = FileUtils.destinationFolder(this, destinationFolder);
                            FileUtils.deleteFolderIfNeeded(folder);
                        }

                        // Create Folders
                        for (String destinationFolder : foldersName) {
                            File folder = FileUtils.destinationFolder(this, destinationFolder);
                            FileUtils.createFolderIfNeeded(folder);
                        }

                        // Tabs Icons
                        for (TabsBean tab : tableConfiguration.getTabs()) {
                            downloadRequest(tab.getIconUrl().getUrlLight(), FileUtils.destinationFolder(this, FileUtils.TAB_ICONs_LIGHT));
                            downloadRequest(tab.getIconUrl().getUrlSelectedDark(), FileUtils.destinationFolder(this, FileUtils.TAB_ICONs_LIGHT));

                            downloadRequest(tab.getIconUrl().getUrlDark(), FileUtils.destinationFolder(this, FileUtils.TAB_ICONs_DARK));
                            downloadRequest(tab.getIconUrl().getUrlSelectedDark(), FileUtils.destinationFolder(this, FileUtils.TAB_ICONs_DARK));

                        }


                        for (int i = 0; i < 2; i++) {
                            OtherIconUrls iconUrls = null;
                            String folderListing;
                            String folderPlaceHolder;
                            String folderTopbar;
                            String folderLogo;

                            File folderListingF;
                            File folderPlaceHolderF;
                            File folderTopbarF;
                            File folderLogoF;

                            if (i == 0) {
                                iconUrls = tableConfiguration.getOtherIconsDownloadUrls().getLight();
                                folderListing = FileUtils.LISTING_ICONs_LIGHT;
                                folderPlaceHolder = FileUtils.PLACE_HOLDER_LIGHT;
                                folderTopbar = FileUtils.TOPBAR_ICONs_LIGHT;
                                folderLogo = FileUtils.LOGOs_LIGHT;

                            } else {
                                iconUrls = tableConfiguration.getOtherIconsDownloadUrls().getDark();

                                folderListing = FileUtils.LISTING_ICONs_DARK;
                                folderPlaceHolder = FileUtils.PLACE_HOLDER_DARK;
                                folderTopbar = FileUtils.TOPBAR_ICONs_DARK;
                                folderLogo = FileUtils.LOGOs_DARK;
                            }

                            folderListingF = FileUtils.destinationFolder(this, folderListing);
                            folderPlaceHolderF = FileUtils.destinationFolder(this, folderPlaceHolder);
                            folderTopbarF = FileUtils.destinationFolder(this, folderTopbar);
                            folderLogoF = FileUtils.destinationFolder(this, folderLogo);

                            downloadRequest(iconUrls.getLogo(), folderLogoF);

                            ListingIconUrl listing = iconUrls.getListing();
                            TopbarIconUrl topbar = iconUrls.getTopbar();
                            PlaceHolder placeHolder = iconUrls.getPlaceHolder();

                            downloadRequest(listing.getUnfavorite(), folderListingF);
                            downloadRequest(listing.getFavorite(), folderListingF);
                            downloadRequest(listing.getDislike(), folderListingF);
                            downloadRequest(listing.getLike(), folderListingF);
                            downloadRequest(listing.getUnbookmark(), folderListingF);
                            downloadRequest(listing.getBookmark(), folderListingF);
                            downloadRequest(listing.getShare(), folderListingF);

                            downloadRequest(topbar.getBookmark(), folderTopbarF);
                            downloadRequest(topbar.getUnbookmark(), folderTopbarF);
                            downloadRequest(topbar.getFavorite(), folderTopbarF);
                            downloadRequest(topbar.getUnfavorite(), folderTopbarF);
                            downloadRequest(topbar.getLike(), folderTopbarF);
                            downloadRequest(topbar.getDislike(), folderTopbarF);
                            downloadRequest(topbar.getCrown(), folderTopbarF);
                            downloadRequest(topbar.getShare(), folderTopbarF);
                            downloadRequest(topbar.getComment(), folderTopbarF);
                            downloadRequest(topbar.getTtsPause(), folderTopbarF);
                            downloadRequest(topbar.getTtsPlay(), folderTopbarF);
                            downloadRequest(topbar.getOverflow_verticle_dot(), folderTopbarF);
                            downloadRequest(topbar.getLeft_slider_three_line(), folderTopbarF);
                            downloadRequest(topbar.getLogo(), folderTopbarF);
                            downloadRequest(topbar.getBack(), folderTopbarF);
                            downloadRequest(topbar.getSearch(), folderTopbarF);
                            downloadRequest(topbar.getTextSize(), folderTopbarF);
                            downloadRequest(topbar.getCross(), folderTopbarF);
                            downloadRequest(topbar.getRefresh(), folderTopbarF);

                            downloadRequest(placeHolder.getWidget(), folderPlaceHolderF);
                            downloadRequest(placeHolder.getThumb(), folderPlaceHolderF);
                            downloadRequest(placeHolder.getAds(), folderPlaceHolderF);
                            downloadRequest(placeHolder.getBanner(), folderPlaceHolderF);

                        }

                        return false;
                    }).subscribe(val -> {

                    },
                    throwable -> {
                        Log.i("", "");
                        Download download = new Download();
                        sendFailedNotification(download);
                    }));
        }

        stopSelf();

        //return super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    private void downloadRequest(String url, File destinationFolder) {
        sendRequestCountNotification();
        if(!URLUtil.isValidUrl(url)) {
            Download download = new Download();
            download.setUrl("Not valid url :: "+url);
            download.setLocalFilePath(destinationFolder.getPath());
            sendFailedNotification(download);
            return;
        }
        Call<ResponseBody> request = retrofitInterface.downloadFile(url);
            request.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        downloadFile(response.body(), destinationFolder, url);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Download download = new Download();
                        download.setLocalFilePath(destinationFolder.getPath());
                        download.setUrl(url);
                        sendFailedNotification(download);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    Download download = new Download();
                    download.setLocalFilePath(destinationFolder.getPath());
                    download.setUrl(url);
                    sendFailedNotification(download);
                }
            });

    }

    private static class DownloadContentPackage {
        ResponseBody body; File destinationFolder; String url;

        public DownloadContentPackage(ResponseBody body, File destinationFolder, String url) {
            this.body = body;
            this.destinationFolder = destinationFolder;
            this.url = url;
        }

        public ResponseBody getBody() {
            return body;
        }

        public File getDestinationFolder() {
            return destinationFolder;
        }

        public String getUrl() {
            return url;
        }
    }

    @Override
    public void onDestroy() {
//        mDisposable.clear();
//        mDisposable.dispose();
        super.onDestroy();
    }

    private void downloadFile(ResponseBody bodyParam, File destinationFolderParam, String urlParam) {
        mDisposable.add(Observable.just(new DownloadContentPackage(bodyParam, destinationFolderParam, urlParam))
                .subscribeOn(Schedulers.io())
                .map(downloadContentPackage -> {
                    ResponseBody body = downloadContentPackage.body;
                    File destinationFolder = downloadContentPackage.destinationFolder;
                    String url = downloadContentPackage.url;
                    String fileName = FileUtils.getFileNameFromUrl(url);
                    int count;
                    byte data[] = new byte[1024 * 4];
                    long fileSize = body.contentLength();
                    InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);
                    File outputFile = new File(destinationFolder, fileName);
                    OutputStream output = new FileOutputStream(outputFile);
                    long total = 0;
                    long startTime = System.currentTimeMillis();
                    int timeCount = 1;
                    Download download = new Download();
                    while ((count = bis.read(data)) != -1) {
                        total += count;
                        totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
                        double current = Math.round(total / (Math.pow(1024, 2)));

                        int progress = (int) ((total * 100) / fileSize);

                        long currentTime = System.currentTimeMillis() - startTime;
                        download.setTotalFileSize(totalFileSize);
                        download.setUrl(url);
                        if (currentTime > 1000 * timeCount) {
                            download.setCurrentFileSize((int) current);
                            download.setStatus(progress);
                            sendProgressNotification(download);
                            timeCount++;
                        }
                        output.write(data, 0, count);
                    }
                    output.flush();
                    output.close();
                    bis.close();
                    return download;
                })
                .subscribe(download -> {
                    onDownloadComplete(download);
                }, throwable -> {
                    Download download = new Download();
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        download.setUrl(httpException.response().raw().request().url().toString());
                    }
                    else {
                        download.setUrl(urlParam);
                    }
                    sendFailedNotification(download);
                }));

    }

    private void sendProgressNotification(Download download){
        Intent intent = new Intent(IconDownloadService.MESSAGE_PROGRESS);
        intent.putExtra("download",download);
        LocalBroadcastManager.getInstance(IconDownloadService.this).sendBroadcast(intent);
        /*notificationBuilder.setProgress(100,download.getStatus(),false);
        notificationBuilder.setContentText(String.format("Downloaded (%d/%d) MB",download.getCurrentFileSize(),download.getTotalFileSize()));
        notificationManager.notify(0, notificationBuilder.build());*/


    }

    private void sendSuccessNotification(Download download){
        Intent intent = new Intent(IconDownloadService.MESSAGE_SUCCESS);
        download.setStatus(100);
        intent.putExtra("download",download);
        LocalBroadcastManager.getInstance(IconDownloadService.this).sendBroadcast(intent);
    }

    private void sendFailedNotification(Download download){
        Intent intent = new Intent(IconDownloadService.MESSAGE_FAILED);
        download.setStatus(0);
        intent.putExtra("download",download);
        LocalBroadcastManager.getInstance(IconDownloadService.this).sendBroadcast(intent);
    }

    private void sendStatusNotification(){
        Download download = new Download();
        download.setStatus(mStatus);
        Intent intent = new Intent(IconDownloadService.MESSAGE_STATUS);
        intent.putExtra("download",download);
        LocalBroadcastManager.getInstance(IconDownloadService.this).sendBroadcast(intent);
    }

    private void sendRequestCountNotification() {
        mRequestCount++;
        Download download = new Download();
        download.setRequestNumber(mRequestCount);
        Intent intent = new Intent(IconDownloadService.MESSAGE_REQUEST_SENT);
        intent.putExtra("download",download);
        LocalBroadcastManager.getInstance(IconDownloadService.this).sendBroadcast(intent);
    }

    private void onDownloadComplete(Download download) {

        sendSuccessNotification(download);

        /*notificationManager.cancel(0);
        notificationBuilder.setProgress(0,0,false);
        notificationBuilder.setContentText("File Downloaded");
        notificationManager.notify(0, notificationBuilder.build());*/

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        //notificationManager.cancel(0);
    }

}
