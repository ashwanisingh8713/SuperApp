package com.ns.contentfragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ns.model.GoldDollarDataModel;
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
import java.text.NumberFormat;
import java.util.Locale;

public class GoldFragment extends Fragment {
    private GetGoldDollarTask mGetForexDataTask;
    private TextView mGoldTextView;
    private TextView mSilverTextView;
    private CustomProgressBar mProgress;
    private LinearLayout mContainer;
    private boolean isFragmentVisible;
    private TextView mLastUpdateDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gold, container, false);
        initView(view);
        /*if (mActivity != null && isFragmentVisible) {
            GoogleAnalyticsTracker.setGoogleAnalyticScreenName(getActivity(), getString(R.string.ga_gold_screen));
            FlurryAgent.logEvent(getString(R.string.ga_gold_screen));
            FlurryAgent.onPageView();
            mActivity.createBannerAdRequest(true, false, Constants.THE_HINDU_URL);

        }*/
        getForexItem();
        return view;
    }

    private void initView(View view) {
        mGoldTextView = (TextView) view.findViewById(R.id.goldValue);
        mSilverTextView = (TextView) view.findViewById(R.id.silverValue);
        mProgress =  view.findViewById(R.id.progress_layout);
        mContainer = (LinearLayout) view.findViewById(R.id.container);
        mLastUpdateDate = (TextView) view.findViewById(R.id.last_update_date);
    }

    private void getForexItem() {
        mProgress.setVisibility(View.VISIBLE);
        mContainer.setVisibility(View.GONE);
        if (mGetForexDataTask == null) {
            mGetForexDataTask = new GetGoldDollarTask();
            mGetForexDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, BLConstants.GOLD_DOLLAR_URL);
        } else if (mGetForexDataTask.getStatus() == AsyncTask.Status.PENDING || mGetForexDataTask.getStatus() == AsyncTask.Status.FINISHED) {
            mGetForexDataTask.cancel(true);
            mGetForexDataTask = new GetGoldDollarTask();
            mGetForexDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, BLConstants.GOLD_DOLLAR_URL);
        }
    }

    private void fillValue(GoldDollarDataModel goldDollarDataModel) {
        mProgress.setVisibility(View.GONE);
        mContainer.setVisibility(View.VISIBLE);
        if (goldDollarDataModel.getData() != null) {
            String goldRate = goldDollarDataModel.getData().getGoldsilver().getGold();
            String silverVal = goldDollarDataModel.getData().getGoldsilver().getSilver();
            String goldUpdatedGold = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(goldRate));
            String goldUpdatedSilver = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(silverVal));
            mGoldTextView.setText("₹ "+goldUpdatedGold);
            mSilverTextView.setText("₹ "+goldUpdatedSilver);
            mLastUpdateDate.setText(goldDollarDataModel.getData().getGoldsilver().getDate());

        } else {
            //mProgress.setErrorText("Failed To Load Quotes.");
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isFragmentVisible = isVisibleToUser;
        /*if (mActivity != null && isVisibleToUser) {
            GoogleAnalyticsTracker.setGoogleAnalyticScreenName(getActivity(), getString(R.string.ga_gold_screen));
            FlurryAgent.logEvent(getString(R.string.ga_gold_screen));
            FlurryAgent.onPageView();
            mActivity.createBannerAdRequest(true, false, Constants.THE_HINDU_URL);
        }*/


    }

    private class GetGoldDollarTask extends AsyncTask<String, Void, GoldDollarDataModel> {
        private static final String TAG = "GetGoldDollarTask";

        @Override
        protected GoldDollarDataModel doInBackground(String... strings) {
            if (isCancelled()) {
                return null;
            }
            GoldDollarDataModel response = null;
            try {

                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = connection.getInputStream();
                    Reader reader = new InputStreamReader(is);
                    Gson gson = new Gson();
                    response = gson.fromJson(reader, GoldDollarDataModel.class);
                }
                if (isCancelled()) {
                    return null;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalStateException | JsonSyntaxException exception) {
                exception.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(GoldDollarDataModel goldDollarDataModel) {
            super.onPostExecute(goldDollarDataModel);
            if (goldDollarDataModel != null) {
                fillValue(goldDollarDataModel);
            } else {
                //mProgress.setErrorText("Failed To Load Quotes.");
            }

        }
    }
}
