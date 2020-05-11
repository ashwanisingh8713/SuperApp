package com.ns.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.netoperation.model.TxnDataBean;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.loginfragment.RecoPlansWebViewFragment;
import com.ns.model.SubscriptionPlanModel;
import com.ns.thpremium.R;
import com.ns.utils.CommonUtil;
import com.ns.utils.FragmentUtil;
import com.ns.utils.ResUtil;
import com.ns.view.text.CustomTextView;

import java.util.ArrayList;

public class SubscriptionPlanAdapter extends BaseRecyclerViewAdapter {

    public static final int VT_HORIZONTAL_TITLE = 1;
    public static final int VT_HORIZONTAL_RECYCLER = 2;
    public static final int VT_PURCHASED_TITLE = 3;
    public static final int VT_PURCHASED_ITEM = 4;

    private ArrayList<SubscriptionPlanModel> subscriptionPlanModels;

    public SubscriptionPlanAdapter(ArrayList<SubscriptionPlanModel> txnDataBeans) {
        this.subscriptionPlanModels = txnDataBeans;
    }

    @Override
    public int getItemViewType(int position) {
        return subscriptionPlanModels.get(position).getVtType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if(viewType == VT_HORIZONTAL_TITLE) {
            return new TitleSubTitleHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.title_subtitle_only, viewGroup, false));
        } else if(viewType == VT_HORIZONTAL_RECYCLER) {
            return new RecyclerViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.recyclerview_only, viewGroup, false));
        } else if(viewType == VT_PURCHASED_TITLE) {
            return new TitleSubTitleHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.title_subtitle_only, viewGroup, false));
        } else if(viewType == VT_PURCHASED_ITEM) {
            return new SubsPlanViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_transaction_history_subs_plan, viewGroup, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if(viewHolder instanceof RecyclerViewHolder) {
            RecyclerViewHolder holder = (RecyclerViewHolder)viewHolder;
            UserPlanAdapter adapter = new UserPlanAdapter(subscriptionPlanModels.get(position).getUserPlanDataBean());
            holder.recyclerViewHorizontal.setAdapter(adapter);
        }
        else if(viewHolder instanceof SubsPlanViewHolder) {
            TxnDataBean bean = subscriptionPlanModels.get(position).getTxnDataBean();
            SubsPlanViewHolder holder = (SubsPlanViewHolder) viewHolder;
            holder.packName_Txt.setText(bean.getPlanName());
            holder.date_Txt.setText("Start Date  "+bean.getsDate());
            holder.endDate_Txt.setText("End Date  "+(bean.geteDate() != null ? bean.geteDate() : ""));

            if(bean.getIsActive() == 1) {
                holder.status_Txt.setTextColor(ResUtil.getColor(holder.itemView.getResources(), R.color.greenColor_1));
                holder.status_Txt.setText("Active");
            } else if(bean.getIsActive() == 0 && CommonUtil.isStartDateFutureDate(bean.getsDate())) {
                holder.status_Txt.setTextColor(ResUtil.getColor(holder.itemView.getResources(), R.color.yellow_yet_to_active));
                holder.status_Txt.setText("Yet to Active");
            } else if(bean.getIsActive() == 0 && !CommonUtil.isStartDateFutureDate(bean.getsDate())) {
                holder.status_Txt.setTextColor(ResUtil.getColor(holder.itemView.getResources(), R.color.redColor_1));
                holder.status_Txt.setText("Expired");
            } else {
                holder.status_Txt.setTextColor(ResUtil.getColor(holder.itemView.getResources(), R.color.grey));
                holder.status_Txt.setText(bean.getStatus());
            }

        }
         else if(viewHolder instanceof TitleSubTitleHolder) {
             TitleSubTitleHolder holder = (TitleSubTitleHolder) viewHolder;
            holder.headerTitle_Txt.setText(subscriptionPlanModels.get(position).getTitle());
            holder.headerSubTitle.setText(subscriptionPlanModels.get(position).getSubTitle());
            if (subscriptionPlanModels.get(position).getSubTitle().equalsIgnoreCase("Currently you don't have any active plan")) {
                holder.currentPackLayout.setVisibility(View.VISIBLE);
                holder.buttonPurchaseNow.setOnClickListener(view -> {
                    //IntentUtil.openSubscriptionActivity(view.getContext(), THPConstants.FROM_SUBSCRIPTION_EXPLORE);
                    RecoPlansWebViewFragment fragment = RecoPlansWebViewFragment.getInstance("", null);
                    FragmentUtil.addFragmentAnim((AppCompatActivity)view.getContext(), R.id.parentLayout, fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
                });
            } else {
                holder.currentPackLayout.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return subscriptionPlanModels.size();
    }


    private class RecyclerViewHolder extends RecyclerView.ViewHolder {

        protected RecyclerView recyclerViewHorizontal;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerViewHorizontal = itemView.findViewById(R.id.recyclerViewHorizontal);
            LinearLayoutManager llm = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerViewHorizontal.setLayoutManager(llm);

        }

    }

    private static class SubsPlanViewHolder extends RecyclerView.ViewHolder {

        TextView packName_Txt;
        TextView status_Txt;
        TextView date_Txt, endDate_Txt;

        public SubsPlanViewHolder(@NonNull View itemView) {
            super(itemView);
            packName_Txt = itemView.findViewById(R.id.orderId_Txt);
            status_Txt = itemView.findViewById(R.id.status_Txt);
            date_Txt = itemView.findViewById(R.id.date_Txt);
            endDate_Txt = itemView.findViewById(R.id.end_date_Txt);

        }
    }

    private static class TitleSubTitleHolder extends RecyclerView.ViewHolder {

        CustomTextView headerTitle_Txt, headerSubTitle, buttonPurchaseNow;
        View currentPackLayout;
        TitleSubTitleHolder(@NonNull View itemView) {
            super(itemView);
            headerTitle_Txt = itemView.findViewById(R.id.headerTitle);
            headerSubTitle = itemView.findViewById(R.id.headerSubtitle);
            currentPackLayout = itemView.findViewById(R.id.currentPlanLayout);
            buttonPurchaseNow = itemView.findViewById(R.id.button_SubscribeNow);
        }
    }


}
