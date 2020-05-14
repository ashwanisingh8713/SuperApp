package com.ns.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.main.SuperApp;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.netoperation.asynctasks.GetCompanyNameTask;
import com.netoperation.asynctasks.SearchAdapter;
import com.netoperation.db.THPDB;
import com.netoperation.model.ArticleBean;
import com.netoperation.model.SectionAdapterItem;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.util.NetConstants;
import com.netoperation.util.DefaultPref;
import com.ns.adapter.SectionContentAdapter;
import com.ns.alerts.Alerts;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.model.CompanyData;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.BLConstants;
import com.ns.utils.CommonUtil;
import com.ns.utils.NetUtils;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.view.RecyclerViewPullToRefresh;
import com.ns.view.layout.NSLinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Case;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;

public class SearchActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {

    private MaterialSearchBar searchBar;
    private RecyclerViewPullToRefresh mPullToRefreshLayout;
    private LinearLayout emptyLayout;

    private SectionContentAdapter mRecyclerAdapter;
    private String mSearchType = "Article";
    private List<String> mListSearchTypes = new ArrayList<>();
    private String mSearchUrlByText;
    private PopupWindow popupWindow;


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

        searchBar.setHint("Search for " + mSearchType);

        boolean isDayTheme = DefaultPref.getInstance(this).isUserThemeDay();
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
            searchBar.setArrowIcon(R.drawable.ic_arrow_back_dark);
            searchBar.setClearIcon(R.drawable.ic_close_search);
            searchBar.setMenuIcon(R.drawable.ic_more_w);
        }

        // Setting Search Pop up option from Menu XML
         searchBar.inflateMenu(R.menu.search_option_menu);
        //registering popup with OnMenuItemClickListener
        searchBar.getMenu().setOnMenuItemClickListener(item -> {
            Toast.makeText(SearchActivity.this,"You have selected : " + item.getTitle(), Toast.LENGTH_SHORT).show();
            item.setChecked(true);
            mSearchType = item.getTitle().toString();
            searchBar.setHint("Search for "+mSearchType);
            return true;
        });
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!s.toString().isEmpty()) {
                    if (!mSearchType.equalsIgnoreCase("Article")) {
                        searchStocksByText(s.toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mRecyclerAdapter = new SectionContentAdapter(NetConstants.GROUP_DEFAULT_SECTIONS, new ArrayList<>(), false, null, null);
        mPullToRefreshLayout.setDataAdapter(mRecyclerAdapter);

        //registerEmptyView();


        //Fetch Search Options from DB
        THPDB.getInstance(this).daoConfiguration().getConfigurationSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((tableConfiguration, throwable) -> {
                    Log.i("", "");
                    mListSearchTypes = tableConfiguration.getSearchOption().getTypes();
                    if (mListSearchTypes.size() > 0) {
                        mSearchType = mListSearchTypes.get(0);
                    }
                    mSearchUrlByText = tableConfiguration.getSearchOption().getUrlText();
                    updateSearchTypeUI();
                });

        //Fetch Stocks API for BL
        if (BuildConfig.IS_BL) {
            Realm mRealm = SuperApp.getRealmInstance();
            OrderedRealmCollection<CompanyData> companyList = mRealm.where(CompanyData.class).findAll();
            if (companyList.size() <= 0) {
                new GetCompanyNameTask().execute(BLConstants.COMPANY_NAME_LIST_URL);
            }
        }

    }

    private void updateSearchTypeUI() {
        if (popupWindow != null) {
            return;
        }
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.layout_radio_group, null, true);
        RadioGroup rg = new RadioGroup(this);
        NSLinearLayout nl = layout.findViewById(R.id.radioGroupLayout);
        for (String type : mListSearchTypes) {
            RadioButton rb = new RadioButton(this);
            rb.setText(ResUtil.capitalizeFirstLetter(type));
            rb.setTag(type);
            rb.setPadding(10,20,10,10);
            rg.addView(rb);
        }
        rg.setOrientation(RadioGroup.VERTICAL);
        rg.setGravity(Gravity.START);

        NSLinearLayout.LayoutParams params = new NSLinearLayout.LayoutParams(NSLinearLayout.LayoutParams.WRAP_CONTENT, NSLinearLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 10;
        params.rightMargin = 60;
        params.topMargin = 20;
        params.bottomMargin = 30;

        rg.setLayoutParams(params);
        nl.addView(rg);

        rg.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = group.findViewById(checkedId);
            mSearchType = radioButton.getText().toString();
            searchBar.setHint("Search for "+mSearchType);
            if (popupWindow != null)
            popupWindow.dismiss();
        });

        RadioButton rb1 = rg.findViewWithTag(mSearchType);
        if (rb1 != null) {
            rb1.setChecked(true);
        }
        popupWindow = new PopupWindow(this);
        //Inflating the Popup using xml file
        popupWindow.setContentView(layout);
        popupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.shadow_143418));
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        popupWindow.setOverlapAnchor(true);
        //Set PopUpWindow
        searchBar.setPopUpWindow(popupWindow);
    }

    private void registerEmptyView() {
        BaseFragmentTHP fragmentTHP = new BaseFragmentTHP() {
            @Override
            public int getLayoutRes() {
                return 0;
            }
        };

        fragmentTHP.showEmptyLayout(emptyLayout, false, mRecyclerAdapter, mPullToRefreshLayout, false, "");

        fragmentTHP.setEmptyViewClickListener(new BaseFragmentTHP.EmptyViewClickListener() {
            @Override
            public void onEmptyRefreshBtnClick() {

            }

            @Override
            public void onOtherStuffWork() {

            }
        });
    }

    private void showContentView() {
        mPullToRefreshLayout.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
    }

    private void searchArticleByText(String query) {
        final ProgressDialog progress = Alerts.showProgressDialog(SearchActivity.this);
        // Making Server request to get Article from server
        // and Saving into DB, with SectionName = "tempSec"
        Observable<List<ArticleBean>> observable = DefaultTHApiManager.articleFromServer(query, BuildConfig.PRODUCTION_SEARCH_BY_ARTICLE_TEXT_URL);
        observable.timeout(15, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articleBeans -> {
                            mRecyclerAdapter.deleteAllItems();
                            if (articleBeans.size() > 0) {
                                showContentView();
                                for (ArticleBean bean : articleBeans) {
                                    final String itemRowId = "defaultRow_" + bean.getSid() + "_" + bean.getAid();
                                    SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_SEARCH_ROW, itemRowId);
                                    item.setArticleBean(bean);
                                    mRecyclerAdapter.addSingleItem(item);
                                }
                                mPullToRefreshLayout.setDataAdapter(mRecyclerAdapter);
                            } else {
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

    /**
     * Update UI For Stocks SearchType
     */
    private void searchStocksByText(String searchText) {
        Realm mRealm = SuperApp.getRealmInstance();
        OrderedRealmCollection<CompanyData> companyList = mRealm.where(CompanyData.class)
                .contains("name", searchText, Case.INSENSITIVE).findAll();

        if (companyList.size() > 0) {
            showContentView();
            SearchAdapter mSearchCompanyAdapter = new SearchAdapter(this, companyList);
            mPullToRefreshLayout.setDataAdapter(mSearchCompanyAdapter);
        } else {
            registerEmptyView();
        }
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        Log.i("", "");
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        if(NetUtils.isConnected(this)) {
            if (mSearchType.equalsIgnoreCase("Article")) {
                searchArticleByText(text.toString());
            } else {
                searchStocksByText(text.toString());
            }
        }
        else {
            Alerts.noConnectionSnackBar(mPullToRefreshLayout, this);
        }
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
