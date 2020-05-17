/*
 * Copyright (c) 2014 Mobstac TM
 * All Rights Reserved.
 * @since Nov 21, 2014 
 * @author rajeshcp
 */
package com.ns.contentfragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.ns.adapter.ForexAdapter;
import com.ns.model.ForexData;
import com.ns.thpremium.R;
import com.ns.utils.BLConstants;
import com.ns.view.CustomProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ForexFragment extends Fragment {

    private ForexAdapter mAdpater;
    private List<ForexData.Data> forexDatas;
    private ListView listView;
    private GetForexDataTask mGetForexDataTask;
    private CustomProgressBar mProgressView;
    private boolean isFragmentVisible;
    private TextView mLastUpdateDate;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.fragment_forex, container, false);
        listView = layoutView.findViewById(R.id.forex_list);
        mProgressView = layoutView.findViewById(R.id.progress_container);
        mLastUpdateDate = layoutView.findViewById(R.id.last_update_date);
        getForexItem();


        return layoutView;
    }

    private void getForexItem() {
        if (mGetForexDataTask == null) {
            mGetForexDataTask = new GetForexDataTask();
            mGetForexDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, BLConstants.ForexData_URL);
        } else if (mGetForexDataTask.getStatus() == AsyncTask.Status.PENDING || mGetForexDataTask.getStatus() == AsyncTask.Status.FINISHED) {
            mGetForexDataTask.cancel(true);
            mGetForexDataTask = new GetForexDataTask();
            mGetForexDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, BLConstants.ForexData_URL);
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isFragmentVisible = isVisibleToUser;
        /*if (mActivity != null && isVisibleToUser) {
            GoogleAnalyticsTracker.setGoogleAnalyticScreenName(getActivity(), getString(R.string.ga_forex_screen));
            FlurryAgent.logEvent(getString(R.string.ga_forex_screen));
            FlurryAgent.onPageView();
            mActivity.createBannerAdRequest(true, false, BLConstants.THE_HINDU_URL);
        }*/
    }

    private class GetForexDataTask extends AsyncTask<String, Void, ForexData> {

        @Override
        protected ForexData doInBackground(String... strings) {
            if (isCancelled()) {
                return null;
            }
            ForexData response = null;
            try {

                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = connection.getInputStream();
                    Reader reader = new InputStreamReader(is);
                    Gson gson = new Gson();
                    response = gson.fromJson(reader, ForexData.class);
                }
                if (isCancelled()) {
                    return null;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(ForexData forexData) {
            super.onPostExecute(forexData);
            if (isCancelled()) {
                return;
            }

            if (forexData != null) {
                mLastUpdateDate.setText(forexData.getLastUpdatedDate());
                mProgressView.setVisibility(View.GONE);
                forexDatas = forexData.getData();
                listView.setVisibility(View.VISIBLE);
                mAdpater = new ForexAdapter(getActivity(), forexDatas);
                listView.setAdapter(mAdpater);
            } else {
                //mProgressView.setErrorText("Failed To Load Content !!");
            }
        }
    }
}
