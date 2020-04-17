package com.ns.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.ns.adapter.SectionContentAdapter;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.thpremium.R;
import com.ns.view.RecyclerViewPullToRefresh;
import com.ns.view.text.CustomTextView;

public class SearchActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {

    private MaterialSearchBar searchBar;
    private RecyclerViewPullToRefresh mPullToRefreshLayout;
    private ImageView emptyIcon;
    private CustomTextView emptyTitleTxt;
    private CustomTextView emptySubTitleTxt;
    private CustomTextView emptyBtnTxt;
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

        // Setting Search Pop up option from Menu XML
         searchBar.inflateMenu(R.menu.search_option_menu);
        //registering popup with OnMenuItemClickListener
        searchBar.getMenu().setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(SearchActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        searchBar.openSearch();
        searchBar.setOnSearchActionListener(this);

        registerEmptyView();

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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onSearchStateChanged(boolean enabled) {
        Log.i("", "");
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        Log.i("", "");
//        searchBar.hideSuggestionsList();
    }

    @Override
    public void onButtonClicked(int buttonCode) {
        Log.i("", "");
        if(buttonCode == MaterialSearchBar.BUTTON_BACK ) {
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }
}
