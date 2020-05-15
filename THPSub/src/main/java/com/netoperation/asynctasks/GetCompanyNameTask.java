package com.netoperation.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.main.SuperApp;
import com.ns.model.CompanyData;
import com.ns.model.CompanyNameModel;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import io.realm.Realm;

/**
 * Created loading {@link CompanyData}
 * @author NS
 */

public class GetCompanyNameTask extends AsyncTask<String, Void, CompanyNameModel> {

    @Override
    protected CompanyNameModel doInBackground(String... params) {
        CompanyNameModel response = null;
        try {

            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = connection.getInputStream();
                Reader reader = new InputStreamReader(is);
                Gson gson = new Gson();
                response = gson.fromJson(reader, CompanyNameModel.class);
            }
            if (isCancelled()) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(final CompanyNameModel companyNameModel) {
        super.onPostExecute(companyNameModel);
        if (isCancelled()) {
            return;
        }
        if (companyNameModel != null) {
            final Realm mRealm = SuperApp.getRealmInstance();
            mRealm.executeTransactionAsync(realm -> {
                List<CompanyData> mData = companyNameModel.getData();
                realm.copyToRealmOrUpdate(mData);
            });
        }
    }
}
