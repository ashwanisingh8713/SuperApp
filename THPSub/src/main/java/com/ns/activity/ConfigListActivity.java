package com.ns.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.netoperation.model.ConfigurationListData;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.net.RequestCallback;
import com.netoperation.util.DefaultPref;
import com.ns.alerts.Alerts;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.IntentUtil;
import com.ns.view.CustomProgressBar;
import com.ns.view.text.CustomTextView;

import java.util.List;

public class ConfigListActivity extends BaseAcitivityTHP {

    @Override
    public int layoutRes() {
        return R.layout.activity_configuration_list;
    }

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ConfigurationAdapter mAdapter;
    private CustomProgressBar progressBar;

    private CustomTextView envSaveBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView = findViewById(R.id.configRecyclerView);
        envSaveBtn = findViewById(R.id.envSaveBtn);
        progressBar = findViewById(R.id.progressBar);

        if (BuildConfig.IS_BL) {
            getDetailToolbar().showBackTitleIcons("BusinessLine Configuration List", backBtn -> {
                finish();
            });
        } else {
            getDetailToolbar().showBackTitleIcons("TheHindu Configuration List", backBtn -> {
                finish();
            });
        }
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        if (BaseAcitivityTHP.sIsOnline) {
            DefaultTHApiManager.getConfigurationList(new RequestCallback<ConfigurationListData>() {
                @Override
                public void onNext(ConfigurationListData configurationListData) {
                    List<ConfigurationListData.DATABean.ConfigurationsBean> mDataset = configurationListData.getDATA().getConfigurations();
                    int selectedPosition = 0;
                    String selectedConfigId = DefaultPref.getInstance(ConfigListActivity.this).getConfigurationId();
                    for (ConfigurationListData.DATABean.ConfigurationsBean bean : mDataset) {
                        if (bean.getID().equalsIgnoreCase(selectedConfigId)) {
                            break;
                        }
                        selectedPosition++;
                    }
                    mAdapter = new ConfigurationAdapter(mDataset, selectedPosition);
                    recyclerView.setAdapter(mAdapter);
                }

                @Override
                public void onError(Throwable t, String str) {
                    Alerts.showSnackbar(ConfigListActivity.this, getString(R.string.something_went_wrong));
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onComplete(String str) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            Alerts.noConnectionSnackBar(recyclerView, ConfigListActivity.this);
        }

        envSaveBtn.setOnClickListener(v -> {
            DefaultPref.getInstance(this).setConfigurationId(mAdapter.getSelectedConfigurationId());
            startActivity(new Intent(ConfigListActivity.this, SplashActivity.class));
            IntentUtil.clearAllPreviousActivity(this);
        });

    }


    /**
     * Configuration List Adapter
     */
    public class ConfigurationAdapter extends RecyclerView.Adapter<ConfigurationAdapter.MyViewHolder> {

        private List<ConfigurationListData.DATABean.ConfigurationsBean> mDataset;
        private int mSelectedPosition;

        public ConfigurationAdapter(List<ConfigurationListData.DATABean.ConfigurationsBean> myDataset, int selectedPosition) {
            mDataset = myDataset;
            mSelectedPosition = selectedPosition;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name_txt;
            public TextView id_txt;
            public RadioButton radioButton;

            public MyViewHolder(View view) {
                super(view);
                name_txt = view.findViewById(R.id.name_txt);
                id_txt = view.findViewById(R.id.id_txt);
                radioButton = view.findViewById(R.id.radioButton);
            }
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_configuration_list, parent, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.name_txt.setText("Name : " + mDataset.get(position).getNAME());
            holder.id_txt.setText("ID : " + mDataset.get(position).getID());

            if (mSelectedPosition == position) {
                holder.radioButton.setChecked(true);
            } else {
                holder.radioButton.setChecked(false);
            }

            holder.itemView.setOnClickListener(v -> {
                mSelectedPosition = position;
                notifyDataSetChanged();
            });

            holder.radioButton.setOnClickListener(v -> {
                mSelectedPosition = position;
                notifyDataSetChanged();
            });

        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        public String getSelectedConfigurationId() {
            if (mSelectedPosition != -1) {
                return mDataset.get(mSelectedPosition).getID();
            }
            return BuildConfig.CONFIG_PRODUCTION_ID;
        }

    }

}
