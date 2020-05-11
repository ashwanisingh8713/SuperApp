package com.ns.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.netoperation.model.TxnDataBean;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.alerts.Alerts;
import com.ns.loginfragment.RecoPlansWebViewFragment;
import com.ns.thpremium.R;
import com.ns.utils.CommonUtil;
import com.ns.utils.FragmentUtil;
import com.ns.utils.NetUtils;
import com.ns.view.text.CustomTextView;

import java.util.ArrayList;

public class UserPlanAdapter extends BaseRecyclerViewAdapter {

    private ArrayList<TxnDataBean> txnDataBeans;

    public UserPlanAdapter(ArrayList<TxnDataBean> txnDataBeans) {
        this.txnDataBeans = txnDataBeans;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CurrentSubPackHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.current_subscription_pack, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        CurrentSubPackHolder holder = (CurrentSubPackHolder) viewHolder;
        TxnDataBean planBean = txnDataBeans.get(i);
        holder.mPackName.setText(planBean.getPlanName());
        holder.planValidity_Txt.setText(planBean.getValidity());
        //holder.daysLeftText.setText(""+CommonUtil.numDaysFrmCurrentDate(planBean.geteDate()));
        if (planBean.getIsActive() == 1) {
            holder.statusText.setText(R.string.active);
            holder.statusImageView.setImageResource(R.drawable.ic_ok_white);
            holder.daysLeftText.setText(""+CommonUtil.numDaysFrmCurrentDate(planBean.geteDate()));
        } else if (planBean.getIsActive() == 0 && CommonUtil.isStartDateFutureDate(planBean.getsDate())) {
            holder.statusText.setText(R.string.yet_to_active);
            holder.statusImageView.setImageResource(R.drawable.ic_ok_white);
            holder.daysLeftText.setText(""+CommonUtil.numDaysFrmCurrentDate(planBean.getsDate()));
        } else {
            holder.statusText.setText(R.string.expired);
            holder.statusImageView.setImageResource(R.drawable.ic_cross_red);
            holder.daysLeftText.setText("00");
        }
        holder.buttonPurchase.setOnClickListener(view -> {
                    if(NetUtils.isConnected(view.getContext())) {
                        RecoPlansWebViewFragment fragment = RecoPlansWebViewFragment.getInstance("",null);
                        FragmentUtil.addFragmentAnim((AppCompatActivity)view.getContext(), R.id.parentLayout, fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
                    } else {
                        Alerts.noConnectionSnackBar(view, (AppCompatActivity) view.getContext());
                    }
                }
                );
        if (planBean.getPlanId().equalsIgnoreCase("10") || planBean.getPlanName().equalsIgnoreCase("30 days free trial")) {
            holder.buttonPurchase.setVisibility(View.VISIBLE);
            holder.buttonPurchase.setText(R.string.subscribe_now);
        } else {
            holder.buttonPurchase.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return txnDataBeans.size();
    }

    private class CurrentSubPackHolder extends RecyclerView.ViewHolder {

        CustomTextView mPackName, planValidity_Txt, daysLeftText, statusText, buttonPurchase;
        AppCompatImageView statusImageView;

        CurrentSubPackHolder(@NonNull View itemView) {
            super(itemView);
            mPackName = itemView.findViewById(R.id.packName_Txt);
            planValidity_Txt = itemView.findViewById(R.id.planValidity_Txt);
            daysLeftText = itemView.findViewById(R.id.dayLeftTextView);
            statusText = itemView.findViewById(R.id.status_Txt);
            statusImageView = itemView.findViewById(R.id.status_Image);
            buttonPurchase = itemView.findViewById(R.id.button_SubscribeNow);
        }

    }
}
