package com.ns.activity;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.netoperation.config.model.Breadcrumb;
import com.netoperation.default_db.TableConfiguration;
import com.netoperation.model.ArticleBean;
import com.netoperation.model.SectionAdapterItem;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.util.NetConstants;
import com.netoperation.util.DefaultPref;
import com.ns.adapter.SectionContentAdapter;
import com.ns.alerts.Alerts;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.CommonUtil;
import com.ns.utils.NetUtils;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.view.RecyclerViewPullToRefresh;
import com.ns.view.TopbarIconView;
import com.ns.view.TopbarSearch;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SearchActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    //private TopbarSearch searchBar;
    private RecyclerViewPullToRefresh mPullToRefreshLayout;
    private LinearLayout emptyLayout;

    private SectionContentAdapter mRecyclerAdapter;

    private TopbarIconView mBackImageView;
    private TopbarIconView action_crossBtn;
    private TopbarIconView action_overflow;
    private EditText searchEditText;


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
        mPullToRefreshLayout.enablePullToRefresh(false);

        searchEditText.setHint("Enter your keyword...");

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

        /*searchBar.inflateMenu(R.menu.search_option_menu);
        searchBar.getMenu().setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(SearchActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/

        mRecyclerAdapter = new SectionContentAdapter(NetConstants.GROUP_DEFAULT_SECTIONS, new ArrayList<>(), false, null, null);
        mPullToRefreshLayout.setDataAdapter(mRecyclerAdapter);

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
                                for (ArticleBean bean : articleBeans) {
                                    final String itemRowId = "defaultRow_" + bean.getSid() + "_" + bean.getAid();
                                    SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_SEARCH_ROW, itemRowId);
                                    item.setArticleBean(bean);
                                    mRecyclerAdapter.addSingleItem(item);
                                }
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
                                CommonUtil.hideKeyboard(mPullToRefreshLayout);
                            }
                        });
    }






    public void onSearchConfirmed(CharSequence text) {
        if(NetUtils.isConnected(this)) {
            searchArticleByText(text.toString());
        }
        else {
            Alerts.noConnectionSnackBar(mPullToRefreshLayout, this);
        }
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        onSearchConfirmed(searchEditText.getText());
        return true;
    }
}
