package com.ns.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import com.main.SuperApp;
import com.ns.thpremium.BuildConfig;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;

public class RealmSupport {

    private RealmConfiguration mRealmConfiguration;
    private Realm mRealm;



    private void initRealm() {
        ActivityManager activityManager =
                (ActivityManager) SuperApp.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : activityManager.getRunningAppProcesses()) {
            if (android.os.Process.myPid() == processInfo.pid) {
                if (TextUtils.equals(processInfo.processName, BuildConfig.APPLICATION_ID)) {
                    Realm.init(SuperApp.getAppContext());
                    //Realm.setDefaultConfiguration(getMyRealmConfiguration());
                    Realm.setDefaultConfiguration(getMyRealmConfiguration());
                }
                break;
            }
        }
    }



    /**
     * Get Realm Instance
     * @return Realm*/
    public Realm getRealmInstance() {
        initRealm();
        if (mRealm == null) {
            mRealm = Realm.getInstance(getMyRealmConfiguration());
            return mRealm;
        }
        return mRealm;
    }
    /**
     * Get Realm Configuration
     * @return RealmConfiguration*/
    private RealmConfiguration getMyRealmConfiguration() {
        String realmDBName = "t";
        int DATABASE_SCHEMA_VERSION = 1;
        RealmMigration realmMigration;
        if(BuildConfig.IS_BL) {
            realmDBName = "TheHinduBusinessline.realm";
            DATABASE_SCHEMA_VERSION = 6;
            realmMigration = new BusinessLineRealmMigration();
        } else {
            realmDBName = "TheHindu.realm";
            DATABASE_SCHEMA_VERSION = 15;
            realmMigration = new TheHinduRealmMigration();
        }

        if (mRealmConfiguration == null) {
            mRealmConfiguration = new RealmConfiguration.Builder() //.deleteRealmIfMigrationNeeded()
                    .name(realmDBName)
                    .schemaVersion(DATABASE_SCHEMA_VERSION)
                    .migration(realmMigration)
                    .build();
        }
        return mRealmConfiguration;
    }
}
