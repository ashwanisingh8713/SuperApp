package com.ns.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.ns.thpremium.R;

public class SearchActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {
    MaterialSearchBar searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchBar = findViewById(R.id.searchBar);

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
