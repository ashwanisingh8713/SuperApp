package com.ns.contentfragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoSection;
import com.netoperation.default_db.DaoWidget;
import com.netoperation.default_db.TableWidget;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.util.NetConstants;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.thpremium.R;
import com.ns.view.RecyclerViewPullToRefresh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class SectionFragment extends BaseFragmentTHP implements RecyclerViewPullToRefresh.TryAgainBtnClickListener {

    private String mFrom;
    private String mSectionId;
    private boolean mIsSubsection;

    private RecyclerViewPullToRefresh mPullToRefreshLayout;
    private LinearLayout emptyLayout;


    public static SectionFragment getInstance(String from, String sectionId, boolean isSubsection) {
        SectionFragment fragment = new SectionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        bundle.putString("sectionId", sectionId);
        bundle.putBoolean("isSubsection", isSubsection);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_trending;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mFrom = getArguments().getString("from");
            mSectionId = getArguments().getString("sectionId");
            mIsSubsection = getArguments().getBoolean("isSubsection");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPullToRefreshLayout = view.findViewById(R.id.recyclerView);
        emptyLayout = view.findViewById(R.id.emptyLayout);
        mPullToRefreshLayout.setTryAgainBtnClickListener(this);
        mPullToRefreshLayout.hideProgressBar();

        if(mSectionId.equals(NetConstants.RECO_HOME)) {
            DaoWidget daoWidget = THPDB.getInstance(getActivity()).daoWidget();
            daoWidget.getWidgets()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(widgets-> {
                        TableWidget widget1 = widgets.get(0);
                        Map<String, String> sections = new HashMap<>();
                        for(TableWidget widget : widgets) {
                            sections.put(widget.getSecId(), widget.getType());
                        }
                        DefaultTHApiManager.getWidgetContent(getActivity(), sections);
                        return "";
                    })
                    .subscribe(value->{
                        Log.i("", "");
                    }, throwable -> {
                        Log.i("", "");
                    });
        }


    }

    @Override
    public void tryAgainBtnClick() {

    }
}
