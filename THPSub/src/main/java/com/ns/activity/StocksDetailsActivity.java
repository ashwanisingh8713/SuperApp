package com.ns.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.FragmentTabHost;

import com.netoperation.util.DefaultPref;
import com.ns.contentfragment.GetQuoteFragment;
import com.ns.contentfragment.StockDetailsFragment;
import com.ns.thpremium.R;
import com.ns.view.LatoSemiBoldTextView;
import com.ns.view.img.TopbarIconView;
import com.ns.view.TopbarStocks;
import com.ns.view.text.ArticleTitleTextView;

public class StocksDetailsActivity extends BaseAcitivityTHP {
    private TopbarStocks mToolbar;
    private FragmentTabHost mTabHost;
    private LatoSemiBoldTextView mCompanyNameTextView;
    public TopbarIconView refreshMenuItem;


    @Override
    public int layoutRes() {
        return R.layout.stock_details_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar = findViewById(R.id.stocksDetailsToolbar);
        setSupportActionBar(mToolbar);

        TopbarIconView mBackImageView = findViewById(R.id.action_back);
        TopbarIconView searchMenuItem = findViewById(R.id.action_search);
        ArticleTitleTextView titleToolbar = findViewById(R.id.action_titleText);
        refreshMenuItem = findViewById(R.id.action_refresh);
        //refreshMenuItem.setImageResource(R.drawable.ic_refresh_black_24dp);
        titleToolbar.setText("Stock Details");
        mBackImageView.setOnClickListener(view -> {
            finish();
        });
        searchMenuItem.setOnClickListener(view -> {
            Intent intent =new Intent(StocksDetailsActivity.this, SearchActivity.class);
            intent.putExtra("search_key", true);
            startActivity(intent);
        });
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mCompanyNameTextView = findViewById(R.id.companyName);
        int current_tab = 0;
        if (!getIntent().hasExtra("bseCode")) {
            Bundle arguments = new Bundle();
            current_tab = getIntent().getIntExtra("tag", 0);
            arguments.putInt("tag", 0);
            arguments.putString("sortBy", "percentageChange");
            arguments.putString("sortOrder", "desc");
            mTabHost.addTab(mTabHost.newTabSpec("bse").setIndicator("BSE"),
                    StockDetailsFragment.class, arguments);
            arguments = new Bundle();
            arguments.putInt("tag", 1);
            arguments.putString("sortBy", "percentageChange");
            arguments.putString("sortOrder", "desc");
            mTabHost.addTab(mTabHost.newTabSpec("nse").setIndicator("NSE"),
                    StockDetailsFragment.class, arguments);
        } else {
            int bse_code = 0;
            String nse_symbol = "";
            String companyId = "";
            String companyName = "";
            Bundle b = getIntent().getExtras();
            if (b != null) {
                bse_code = b.getInt("bseCode");
                nse_symbol = b.getString("nseSymbol");
                current_tab = b.getInt("tag", 0);
                companyId = b.getString("companyId");
                companyName = b.getString("companyName");
            }
            Bundle arguments = new Bundle();
            arguments.putInt("bseCode", bse_code);
            arguments.putString("nseSymbol", nse_symbol);
            arguments.putString("tab", "BSE");
            arguments.putString("companyId", companyId);
            mTabHost.addTab(mTabHost.newTabSpec("bse").setIndicator("BSE"),
                    GetQuoteFragment.class, arguments);
            arguments = new Bundle();

            arguments.putString("tab", "NSE");
            arguments.putInt("bseCode", bse_code);
            arguments.putString("nseSymbol", nse_symbol);
            arguments.putString("companyId", companyId);
            mTabHost.addTab(mTabHost.newTabSpec("nse").setIndicator("NSE"),
                    GetQuoteFragment.class, arguments);
            mCompanyNameTextView.setText(companyName);
        }
        mTabHost.setCurrentTab(current_tab);
        boolean b = DefaultPref.getInstance(this).isUserThemeDay();
        int textColorCode;
        if (!b) {
            textColorCode = Color.WHITE;
        } else {
            textColorCode = Color.BLACK;

        }

        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            TextView tv = mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(textColorCode);
        }
    }
}
