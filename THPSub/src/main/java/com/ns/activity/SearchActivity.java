package com.ns.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.main.SuperApp;
import com.netoperation.asynctasks.GetCompanyNameTask;
import com.netoperation.asynctasks.SearchAdapter;
import com.netoperation.config.model.Breadcrumb;
import com.netoperation.db.THPDB;
import com.netoperation.default_db.TableConfiguration;
import com.netoperation.model.ArticleBean;
import com.netoperation.model.SectionAdapterItem;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.util.NetConstants;
import com.netoperation.util.DefaultPref;
import com.ns.adapter.SectionContentAdapter;
import com.ns.alerts.Alerts;
import com.ns.model.CompanyData;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.BLConstants;
import com.ns.utils.CommonUtil;
import com.ns.utils.NetUtils;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.view.RecyclerViewPullToRefresh;
import com.ns.view.TopbarIconView;
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

public class SearchActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    private TopbarIconView mBackImageView;
    private TopbarIconView action_crossBtn;
    private TopbarIconView action_overflow;
    private EditText searchEditText;
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

        mBackImageView = findViewById(R.id.action_back);
        action_crossBtn = findViewById(R.id.action_crossBtn);
        action_overflow = findViewById(R.id.action_overflow);
        searchEditText = findViewById(R.id.searchEditText);
        mPullToRefreshLayout = findViewById(R.id.recyclerView);
        emptyLayout = findViewById(R.id.emptyLayout);
        mPullToRefreshLayout.hideProgressBar();

        if (!THPConstants.IS_USE_SEVER_THEME) {
            action_crossBtn.setImageResource(R.drawable.close);
            action_crossBtn.setVisibility(View.GONE);
        }
        action_crossBtn.setOnClickListener(view -> {
            searchEditText.setText("");
            searchEditText.clearFocus();
            action_crossBtn.setVisibility(View.GONE);
        });

        boolean isDayTheme = DefaultPref.getInstance(this).isUserThemeDay();

        TableConfiguration tableConfiguration = BaseAcitivityTHP.getTableConfiguration();
        if(tableConfiguration != null && THPConstants.IS_USE_SEVER_THEME) {
            Breadcrumb breadcrumb = tableConfiguration.getAppTheme().getBreadcrumb();
            String hintColr;
            String textColr;
            if(isDayTheme) {
                hintColr = breadcrumb.getText().getLight();
                textColr = breadcrumb.getText().getLightSelected();
            } else {
                hintColr = breadcrumb.getText().getDark();
                textColr = breadcrumb.getText().getDarkSelected();
            }
            searchEditText.setTextColor(Color.parseColor(textColr));
            searchEditText.setHintTextColor(Color.parseColor(hintColr));

        }
        else {
            if (isDayTheme) {
                searchEditText.setTextColor(ResUtil.getColor(getResources(), R.color.Black));
                searchEditText.setHintTextColor(ResUtil.getColor(getResources(), R.color.search_hint));
            } else {
                searchEditText.setTextColor(ResUtil.getColor(getResources(), R.color.white));
                searchEditText.setHintTextColor(ResUtil.getColor(getResources(), R.color.search_hint));
            }
        }

        searchEditText.setOnEditorActionListener(this);

        mBackImageView.setOnClickListener(v->{
            CommonUtil.hideKeyboard(mPullToRefreshLayout);
            finish();
        });
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!s.toString().isEmpty()) {
                    action_crossBtn.setVisibility(View.VISIBLE);
                    if (!mSearchType.equalsIgnoreCase("Article")) {
                        searchStocksByText(s.toString());
                    }
                } else {
                    action_crossBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mRecyclerAdapter = new SectionContentAdapter(NetConstants.GROUP_DEFAULT_SECTIONS, new ArrayList<>(), false, null, null);
        mPullToRefreshLayout.setDataAdapter(mRecyclerAdapter);

        //Fetch Search Options from DB
        THPDB.getInstance(this).daoConfiguration().getConfigurationSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((tableConfiguration1, throwable) -> {
                    Log.i("", "");
                    mListSearchTypes = tableConfiguration1.getSearchOption().getTypes();
                    if (mListSearchTypes.size() > 0) {
                        mSearchType = mListSearchTypes.get(0);
                    }
                    mSearchUrlByText = tableConfiguration1.getSearchOption().getUrlText();
                });

        //Fetch Stocks API for BL
        if (BuildConfig.IS_BL) {
            Realm mRealm = SuperApp.getRealmInstance();
            OrderedRealmCollection<CompanyData> companyList = mRealm.where(CompanyData.class).findAll();
            if (companyList.size() <= 0) {
                new GetCompanyNameTask().execute(BLConstants.COMPANY_NAME_LIST_URL);
            }
        }


        action_overflow.setOnClickListener(v->{
            updateSearchTypeUI();
        });

    }

    private void updateSearchTypeUI() {
        if (popupWindow != null) {
            popupWindow.showAsDropDown(action_overflow);
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
            searchEditText.setHint("Search for "+mSearchType);
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
        popupWindow.showAsDropDown(action_overflow);
    }

    private void showEmptyView() {
        emptyLayout.setVisibility(View.VISIBLE);
        mPullToRefreshLayout.setVisibility(View.GONE);
    }

    private void showContentView() {
        emptyLayout.setVisibility(View.GONE);
        mPullToRefreshLayout.setVisibility(View.VISIBLE);
    }

    private void searchArticleByText(String query) {
        if(mSearchUrlByText == null) {
            BaseAcitivityTHP.refreshConfigurationInstance();
            Alerts.showToast(this, "Kindly try again...");
            return;
        }
        final ProgressDialog progress = Alerts.showProgressDialog(SearchActivity.this);
        // Making Server request to get Article from server
        // and Saving into DB, with SectionName = "tempSec"
        Observable<List<ArticleBean>> observable = DefaultTHApiManager.articleFromServer(query, mSearchUrlByText);
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
                                showEmptyView();
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
                                CommonUtil.hideKeyboard(mPullToRefreshLayout);
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
            showEmptyView();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        onSearchConfirmed(searchEditText.getText());
        return true;
    }


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


}
