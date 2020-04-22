package com.ns.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.netoperation.model.ArticleBean;
import com.netoperation.model.SectionAdapterItem;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.util.NetConstants;
import com.netoperation.util.UserPref;
import com.ns.adapter.SectionContentAdapter;
import com.ns.alerts.Alerts;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.CommonUtil;
import com.ns.utils.ContentUtil;
import com.ns.utils.IntentUtil;
import com.ns.utils.ResUtil;
import com.ns.view.RecyclerViewPullToRefresh;
import com.ns.view.text.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class SearchActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {

    private MaterialSearchBar searchBar;
    private RecyclerViewPullToRefresh mPullToRefreshLayout;
    private LinearLayout emptyLayout;

    private SectionContentAdapter mRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchBar = findViewById(R.id.searchBar);
        mPullToRefreshLayout = findViewById(R.id.recyclerView);
        emptyLayout = findViewById(R.id.emptyLayout);
        mPullToRefreshLayout.hideProgressBar();

        searchBar.openSearch();
        searchBar.setOnSearchActionListener(this);

        searchBar.setHint("Enter your keyword...");

        boolean isDayTheme = UserPref.getInstance(this).isUserThemeDay();
        if(isDayTheme) {
            searchBar.setTextColor(getResources().getColor(R.color.Black));
            searchBar.setTextHintColor(getResources().getColor(R.color.search_hint));
            searchBar.setSearchBarColor(Color.WHITE);
            searchBar.setArrowIcon(R.drawable.arrow_back);
            searchBar.setClearIcon(R.drawable.ic_close_search);
            searchBar.setMenuIcon(R.drawable.ic_more);
        }
        else {
            searchBar.setTextColor(getResources().getColor(R.color.white));
            searchBar.setTextHintColor(getResources().getColor(R.color.search_hint));
            searchBar.setSearchBarColor(Color.BLACK);
            searchBar.setArrowIcon(R.drawable.ic_arrow_back_w);
            searchBar.setClearIcon(R.drawable.ic_close_search);
            searchBar.setMenuIcon(R.drawable.ic_more_w);
        }

        // Setting Search Pop up option from Menu XML
         searchBar.inflateMenu(R.menu.search_option_menu);
        //registering popup with OnMenuItemClickListener
        searchBar.getMenu().setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(SearchActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        mRecyclerAdapter = new SectionContentAdapter(NetConstants.GROUP_DEFAULT_SECTIONS, new ArrayList<>(), false, null, null);
        mPullToRefreshLayout.setDataAdapter(mRecyclerAdapter);



        //registerEmptyView();

    }

    private void registerEmptyView() {
        BaseFragmentTHP fragmentTHP = new BaseFragmentTHP() {
            @Override
            public int getLayoutRes() {
                return 0;
            }
        };

        fragmentTHP.showEmptyLayout(emptyLayout, false, mRecyclerAdapter, mPullToRefreshLayout, false, "");

        fragmentTHP.setBaseFragmentListener(new BaseFragmentTHP.BaseFragmentListener() {
            @Override
            public void onEmptyRefreshBtnClick() {

            }

            @Override
            public void onOtherStuffWork() {

            }
        });
    }

    private void searchArticleByText(String query) {
        final ProgressDialog progress = Alerts.showProgressDialog(SearchActivity.this);
        // Making Server request to get Article from server
        // and Saving into DB, with SectionName = "tempSec"
        Observable<List<ArticleBean>> observable = DefaultTHApiManager.articleFromServer(query, BuildConfig.PRODUCTION_SEARCH_BY_ARTICLE_TEXT_URL);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(articleBeans -> {
                            if(articleBeans.size() > 0) {
                                for (ArticleBean bean : articleBeans) {
                                    final String itemRowId = "defaultRow_" + bean.getSid() + "_" + bean.getAid();
                                    SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_SEARCH_ROW, itemRowId);
                                    item.setArticleBean(bean);
                                    mRecyclerAdapter.addSingleItem(item);
                                }
                            }
                            else {
                                mRecyclerAdapter.deleteAllItems();
                                registerEmptyView();
                            }
                        }, throwable -> {
                            if (progress != null) {
                                progress.dismiss();
                            }
                            Alerts.showSnackbar(SearchActivity.this, getResources().getString(R.string.something_went_wrong));
                        },
                        () -> {
                            if (progress != null) {
                                progress.dismiss();
                                CommonUtil.hideKeyboard(searchBar);
                            }
                        });
    }



    @Override
    public void onSearchStateChanged(boolean enabled) {
        Log.i("", "");
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        Log.i("", "");
        searchArticleByText(text.toString());
    }

    @Override
    public void onButtonClicked(int buttonCode) {
        Log.i("", "");
        if(buttonCode == MaterialSearchBar.BUTTON_BACK ) {
            CommonUtil.hideKeyboard(searchBar);
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }
}
