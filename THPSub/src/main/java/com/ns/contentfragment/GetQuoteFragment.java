/*
 * Copyright (c) 2014 Mobstac TM
 * All Rights Reserved.
 * @since Nov 19, 2014 
 * @author rajeshcp
 */
package com.ns.contentfragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.ns.activity.StocksDetailsActivity;
import com.ns.model.CompanyBseData;
import com.ns.model.CompanyNseData;
import com.ns.thpremium.R;
import com.ns.utils.NetUtils;
import com.ns.view.ProgressView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class GetQuoteFragment extends Fragment {

    protected boolean mFromSavedInstanceState;
    private ProgressView mProgress;
    private LinearLayout mContainer;
    private View mView;
    private String tab;
    private ArrayList<CompanyBseData> companyBseDatas;
    private ArrayList<CompanyNseData> companyNseDatas;
    private CompanyBseData mObject;
    private CompanyNseData mCompanyNseData;
    private StocksDetailsActivity stockDetailsActivity;
    private String companyId;

    /**
     * @return of type GetQuoteFragment
     * Constructor function
     * @author rajeshcp
     * @since Nov 19, 2014
     */


    public GetQuoteFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        stockDetailsActivity = (StocksDetailsActivity) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        stockDetailsActivity = (StocksDetailsActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFromSavedInstanceState = savedInstanceState != null;

        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.get_quote_fragment, container, false);
        mProgress = (ProgressView) mView.findViewById(R.id.progress_layout);
        mContainer = (LinearLayout) mView.findViewById(R.id.value_container);
        tab = getArguments().getString("tab");
        companyId = getArguments().getString("companyId");
        stockDetailsActivity.refreshMenuItem.setOnClickListener(view -> loadContents());
        if (tab.equalsIgnoreCase("NSE")) {
            if (companyNseDatas == null) {
                loadContents();
            } else {
//                companyNseDatas = getArguments().getParcelableArrayList("nselist");
                mCompanyNseData = companyNseDatas.get(0);
                fillValues();
            }
        } else {
            if (companyBseDatas == null) {
                loadContents();
            } else {
//                companyBseDatas = getArguments().getParcelableArrayList("bselist");
                mObject = companyBseDatas.get(0);
                fillValues();
            }
        }

        return mView;
    }


    private void loadContents() {
        mProgress.setVisibility(View.VISIBLE);
        mContainer.setVisibility(View.GONE);
        mProgress.showProgress();
        String nse_symbol = getArguments().getString("nseSymbol");
        int bse_code = getArguments().getInt("bseCode");
        if (NetUtils.isConnected(getActivity())) {
            if (tab.equalsIgnoreCase("NSE")) {
                new GetCompanyNSEDataTask().execute("http://tab.thehindu.com:8080/businessline/nse?cid=" + companyId + "&service=1y");
            } else {
                new GetCompanyBSEDataTask().execute("http://tab.thehindu.com:8080/businessline/bse?cid=" + companyId + "&service=1y");
            }
        } else {
            mProgress.setErrorText("Failed To Load Quotes.");
        }
    }


    private void fillValues() {

        mProgress.setVisibility(View.GONE);
        mContainer.setVisibility(View.VISIBLE);

        if (tab.equalsIgnoreCase("NSE")) {
            ((TextView) mView.findViewById(R.id.last_traded_price_val)).setText(String.valueOf(mCompanyNseData.getCp()));
            ((TextView) mView.findViewById(R.id.open_price_val)).setText(String.valueOf(mCompanyNseData.getDop()));
            ((TextView) mView.findViewById(R.id.intra_day_high_val)).setText(String.valueOf(mCompanyNseData.getDhi()));
            ((TextView) mView.findViewById(R.id.intra_day_low_val)).setText(String.valueOf(mCompanyNseData.getDlo()));
            ((TextView) mView.findViewById(R.id.prev_day_close_val)).setText(String.valueOf(mCompanyNseData.getBsep()));
            ((TextView) mView.findViewById(R.id.week_high_val)).setText(String.valueOf(mCompanyNseData.getBse52h()));
            ((TextView) mView.findViewById(R.id.week_low_val)).setText(String.valueOf(mCompanyNseData.getBse52l()));
            ((TextView) mView.findViewById(R.id.actual_price)).setText("\u20B9" + String.valueOf(mCompanyNseData.getCp()));

            TextView mChangeTextView = (TextView) mView.findViewById(R.id.changesValue);
            TextView mChangePercentView = (TextView) mView.findViewById(R.id.percentChange);
            ImageView changeIcon = (ImageView) mView.findViewById(R.id.changeIcon);

            double stockChange = mCompanyNseData.getPer();
            double change = mCompanyNseData.getCh();
            float changeIndicator;
            changeIndicator = (float) stockChange;
            if (changeIndicator < 0) {
                mChangeTextView.setText("" + change);
                mChangePercentView.setText("(" + stockChange + "%" + ")");
                mChangeTextView.setTextColor(getResources().getColor(R.color.RedDown));
                mChangePercentView.setTextColor(getResources().getColor(R.color.RedDown));
                changeIcon.setImageResource(R.drawable.loss_arrow);

            } else if (changeIndicator > 0) {
                mChangeTextView.setText("" + change);
                mChangePercentView.setText("(" + stockChange + "%" + ")");
                mChangeTextView.setTextColor(getResources().getColor(R.color.GreenUp));
                mChangePercentView.setTextColor(getResources().getColor(R.color.GreenUp));
                changeIcon.setImageResource(R.drawable.profit_arrow);
            } else {
                mChangeTextView.setText("N.A.");
            }


        } else {
            ((TextView) mView.findViewById(R.id.last_traded_price_val)).setText(String.valueOf(mObject.getCp()));
            ((TextView) mView.findViewById(R.id.open_price_val)).setText(String.valueOf(mObject.getDop()));
            ((TextView) mView.findViewById(R.id.intra_day_high_val)).setText(String.valueOf(mObject.getDhi()));
            ((TextView) mView.findViewById(R.id.intra_day_low_val)).setText(String.valueOf(mObject.getDlo()));
            ((TextView) mView.findViewById(R.id.prev_day_close_val)).setText(String.valueOf(mObject.getBsep()));
            ((TextView) mView.findViewById(R.id.week_high_val)).setText(String.valueOf(mObject.getBse52h()));
            ((TextView) mView.findViewById(R.id.week_low_val)).setText(String.valueOf(mObject.getBse52l()));
            ((TextView) mView.findViewById(R.id.actual_price)).setText("\u20B9" + String.valueOf(mObject.getCp()));
            TextView mChangeTextView = (TextView) mView.findViewById(R.id.changesValue);
            TextView mChangePercentView = (TextView) mView.findViewById(R.id.percentChange);
            ImageView changeIcon = (ImageView) mView.findViewById(R.id.changeIcon);


            double stockChange = mObject.getPer();
            double change = mObject.getCh();
            float changeIndicator;
            changeIndicator = (float) stockChange;
            if (changeIndicator < 0) {
                mChangeTextView.setText("" + change);
                mChangePercentView.setText("(" + stockChange + "%" + ")");
                mChangeTextView.setTextColor(getResources().getColor(R.color.RedDown));
                mChangePercentView.setTextColor(getResources().getColor(R.color.RedDown));
                changeIcon.setImageResource(R.drawable.loss_arrow);

            } else if (changeIndicator > 0) {
                mChangeTextView.setText("" + change);
                mChangePercentView.setText("(" + stockChange + "%" + ")");
                mChangeTextView.setTextColor(getResources().getColor(R.color.GreenUp));
                mChangePercentView.setTextColor(getResources().getColor(R.color.GreenUp));
                changeIcon.setImageResource(R.drawable.profit_arrow);
            } else {
                mChangeTextView.setText("N.A.");
            }


        }


    }


    private class GetCompanyBSEDataTask extends AsyncTask<String, Void, CompanyBseData> {

        private static final String TAG = "GetCompanyBSEDataTask";

        @Override
        protected CompanyBseData doInBackground(String... strings) {
            if (isCancelled()) {
                return null;
            }
            CompanyBseData response = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = connection.getInputStream();
                    Reader reader = new InputStreamReader(is);
                    Gson gson = new Gson();
                    response = gson.fromJson(reader, CompanyBseData.class);
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
        protected void onPostExecute(CompanyBseData s) {
            super.onPostExecute(s);
            if (isCancelled()) {
                return;
            }
            if (!isAdded() && getActivity() != null) {
                return;
            }


            if (s != null) {
                Log.i(TAG, String.valueOf(s.getCp()));
                Log.i(TAG, String.valueOf(s.getPer()));
                mObject = s;
                companyBseDatas = new ArrayList<>();
                companyBseDatas.add(s);
                fillValues();

            }

        }
    }

    private class GetCompanyNSEDataTask extends AsyncTask<String, Void, CompanyNseData> {

        private static final String TAG = "GetCompanyNSEDataTask";

        @Override
        protected CompanyNseData doInBackground(String... strings) {
            if (isCancelled()) {
                return null;
            }
            CompanyNseData response = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = connection.getInputStream();
                    Reader reader = new InputStreamReader(is);
                    Gson gson = new Gson();
                    response = gson.fromJson(reader, CompanyNseData.class);
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
        protected void onPostExecute(CompanyNseData s) {
            super.onPostExecute(s);
            if (isCancelled()) {
                return;
            }
            if (!isAdded() && getActivity() != null) {
                return;
            }


            if (s != null) {
                Log.i(TAG, String.valueOf(s.getCp()));
                Log.i(TAG, String.valueOf(s.getPer()));
                mCompanyNseData = s;
                companyNseDatas = new ArrayList<>();
                companyNseDatas.add(s);
                fillValues();

            }

        }
    }

}
