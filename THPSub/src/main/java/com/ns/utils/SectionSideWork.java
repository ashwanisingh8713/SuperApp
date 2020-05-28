package com.ns.utils;

import android.util.Log;

import com.google.android.gms.ads.AdSize;
import com.main.SuperApp;
import com.netoperation.config.model.WidgetIndex;
import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoSection;
import com.netoperation.default_db.DaoWidget;
import com.netoperation.default_db.TableConfiguration;
import com.netoperation.default_db.TableWidget;
import com.netoperation.model.AdData;
import com.netoperation.model.SectionAdapterItem;
import com.netoperation.model.SectionBean;
import com.netoperation.model.StaticPageUrlBean;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.util.NetConstants;
import com.netoperation.util.PremiumPref;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.adapter.SectionContentAdapter;
import com.ns.thpremium.BuildConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SectionSideWork {

    private int mPage = 1;

    public static int PAGE_SIZE = 10;
    private boolean isLoading;
    private boolean isLastPage;

    private List<SectionBean> mSubSections;
    private StaticPageUrlBean mStaticPageBean;
    private Map<String, String> mWidgetsIdTypeMap;
    private List<AdData> taboolaAdsBeans;
    private List<AdData> dfpAdsBeans;
    private String mSectionId;

    private List<IndexArrange> indexArranges = new ArrayList<>();

    public SectionSideWork(String sectionId) {
        mSectionId = sectionId;
        getStaticPage();
        getSubsections();
        homeWidgetIdTypeMap();
    }

    public int getPage() {
        return mPage;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public void incrementPageCount() {
        mPage++;
    }

    public void resetPageCount() {
        mPage = 1;
    }

    public List<SectionBean> getSubSections() {
        return mSubSections;
    }

    public StaticPageUrlBean getStaticPageBean() {
        return mStaticPageBean;
    }

    public Map<String, String> getWidgetsIdTypeMap() {
        return mWidgetsIdTypeMap;
    }

    public String getSectionId() {
        return mSectionId;
    }

    public void setStaticPageBean(StaticPageUrlBean mStaticPageBean) {
        this.mStaticPageBean = mStaticPageBean;
    }

    public List<AdData> getTaboolaAdsBeans() {
        return taboolaAdsBeans;
    }

    public void addTaboolaAdsBeans(AdData adData) {
        if(this.taboolaAdsBeans == null) {
            this.taboolaAdsBeans = new ArrayList<>();
        }
        this.taboolaAdsBeans.add(adData);
    }

    public List<AdData> getDfpAdsBeans() {
        return dfpAdsBeans;
    }

    public void addDfpAdsBeans(AdData adData) {
        if(this.dfpAdsBeans == null) {
            this.dfpAdsBeans = new ArrayList<>();
        }
        this.dfpAdsBeans.add(adData);
    }

    private void getStaticPage() {
            THPDB.getInstance(SuperApp.getAppContext())
                    .daoSection().getStaticPage(mSectionId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(staticPageUrlBean -> {
                        mStaticPageBean = staticPageUrlBean;
                    }, throwable -> {
                        Log.i("", "");
                    });
        }

    private void getSubsections() {
            Observable.just(mSectionId)
                    .subscribeOn(Schedulers.io())
                    .map(secId -> {
                        THPDB thpdb = THPDB.getInstance(SuperApp.getAppContext());
                        DaoSection daoSection = thpdb.daoSection();
                        return daoSection.getSubSections(secId).getSubSections();
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(val -> {
                        mSubSections = val;
                    }, throwable -> {

                    });
    }


    /**
     * It only send request to server to get Widget Data,
     * but shows in
     * @see  com.ns.contentfragment.SectionFragment#showHomeWidgetsFromObservable()
     */
    private void homeWidgetIdTypeMap() {
            final THPDB thpdb = THPDB.getInstance(SuperApp.getAppContext());
            DaoWidget daoWidget = thpdb.daoWidget();
            daoWidget.getWidgetsSingle()
                    .subscribeOn(Schedulers.io())
                    .map(widgets -> {
                        mWidgetsIdTypeMap = new HashMap<>();
                        for (TableWidget widget : widgets) {
                            mWidgetsIdTypeMap.put(widget.getSecId(), widget.getType());
                        }
                        return "";
                    })
                    .subscribe();


        }


    public void sendRequestToGetHomeWidgetFromServer() {
        if(getWidgetsIdTypeMap() != null) {
            DefaultTHApiManager.widgetContent(SuperApp.getAppContext(), getWidgetsIdTypeMap());
        }
    }

    Map<String, WidgetIndex> widgetIndexMap = new HashMap<>();

    public WidgetIndex getWidgetIndex(String widgetItemRowId) {
        return widgetIndexMap.get(widgetItemRowId);
    }

    public void indexConfig(SectionContentAdapter mRecyclerAdapter, boolean isDayTheme) {
        TableConfiguration tableConfiguration = BaseAcitivityTHP.getTableConfiguration();
        if(tableConfiguration == null) {
            return;
        }

        // Widget Index
        if(mSectionId.equalsIgnoreCase(NetConstants.RECO_HOME_TAB)) {
            List<WidgetIndex> widgetIndices = tableConfiguration.getWidget();
            for (WidgetIndex widgetIndex : widgetIndices) {
                final String itemRowId = RowIds.rowId_widget(widgetIndex.getSecId());
                SectionAdapterItem item = null;
                if(BuildConfig.IS_BL) {
                    item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_BLD_WIDGET_DEFAULT, itemRowId);
                } else {
                    item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_WIDGET_DEFAULT, itemRowId);
                }
                /*if(isDayTheme) {
                    item.setBackgroundColor(widgetIndex.getBackground().getLight());
                    item.setTextColor(widgetIndex.getText().getLight());
                } else {
                    item.setBackgroundColor(widgetIndex.getBackground().getDark());
                    item.setTextColor(widgetIndex.getText().getDark());
                }*/
                int index = widgetIndex.getIndex();
                IndexArrange widgetArrange = new IndexArrange(index, item);
                indexArranges.add(widgetArrange);
                widgetIndexMap.put(itemRowId, widgetIndex);
            }
        }

        // Ads Index
        if(!PremiumPref.getInstance(SuperApp.getAppContext()).isUserAdsFree() && BaseAcitivityTHP.sIsOnline ) {
            for (AdData adsBean : tableConfiguration.getAds().getListingPageAds()) {
                adsBean.setSecId(mSectionId);
                adsBean.setAdDataUiqueId(RowIds.adDataUiqueId(adsBean.getIndex(), adsBean.getType()));
                SectionAdapterItem item;
                if (adsBean.getType().equalsIgnoreCase("DFP")) {
                    adsBean.setAdSize(AdSize.MEDIUM_RECTANGLE);
                    adsBean.setReloadOnScroll(false);
                    addDfpAdsBeans(adsBean);
                    item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_300X250_ADS, adsBean.getAdDataUiqueId());
                } else {
                    addTaboolaAdsBeans(adsBean);
                    item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_TABOOLA_LISTING_ADS, adsBean.getAdDataUiqueId());
                }

                item.setAdData(adsBean);
                item.setProposedIndex(adsBean.getIndex());

                IndexArrange adsArrange = new IndexArrange(adsBean.getIndex(), item);
                indexArranges.add(adsArrange);
            }
        }

        // Static Web Page Index
        if(getStaticPageBean() != null && getStaticPageBean().isIsEnabled() && getStaticPageBean().getPosition() > -1) {
                final String itemRowId = RowIds.rowId_staticWebPage(mSectionId, getStaticPageBean().getPosition());
                SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_WEB_WIDGET, itemRowId);
                IndexArrange staticArrange = new IndexArrange(getStaticPageBean().getPosition(), item);
                indexArranges.add(staticArrange);
        }

        // Sub-Section Index
        if(getSubSections() != null && getSubSections().size() > 0) {
            String indexStr = tableConfiguration.getSubSectionsIndex();
            if(!ResUtil.isEmpty(indexStr)) {
                try {
                    Integer index = Integer.parseInt(indexStr);
                    if(index == -1) {
                        index = 3;
                    }
                    String rowItemId = "subsection_" + mSectionId;
                    SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_HORIZONTAL_LIST, rowItemId);
                    item.setProposedIndex(index);
                    IndexArrange subsectionArrange = new IndexArrange(index, item);
                    indexArranges.add(subsectionArrange);
                } catch (Exception e) {

                }
            }
        }

        Collections.sort(indexArranges);


        loadForAnotherPage(mRecyclerAdapter);


    }


    public void loadForAnotherPage(SectionContentAdapter mRecyclerAdapter) {
        Iterator i = indexArranges.iterator();
        while (i.hasNext()) {
            IndexArrange indexArrange = (IndexArrange)i.next();
            boolean isInserted = mRecyclerAdapter.insertItemAfterArrangingIndex(indexArrange.adapterItem, indexArrange.getIndex());
            if(isInserted) {
                i.remove();
            }
        }
    }



    private static class IndexArrange implements Comparable<IndexArrange> {
        int index;
        SectionAdapterItem adapterItem;

        public IndexArrange(int index, SectionAdapterItem adapterItem) {
            this.index = index;
            this.adapterItem = adapterItem;
        }

        public Integer getIndex() {
            return index;
        }

        public SectionAdapterItem getAdapterItem() {
            return adapterItem;
        }

        @Override
        public int compareTo(IndexArrange o) {
            return this.getIndex().compareTo(o.getIndex());
        }
    }

}
