package com.ns.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netoperation.model.TxnDataBean;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.thpremium.R;
import com.ns.utils.CommonUtil;
import com.ns.utils.ResUtil;

import java.util.List;

public class TransactionHistoryAdapter extends BaseRecyclerViewAdapter {

    private List<TxnDataBean> transactions;
    private String mViewTypeFrom;

    public TransactionHistoryAdapter(List<TxnDataBean> transactions, String viewTypeFrom) {
        this.transactions = transactions;
        this.mViewTypeFrom = viewTypeFrom;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(mViewTypeFrom.equalsIgnoreCase("HISTORY")) {
            return new TxnHistoryViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_transaction_history, viewGroup, false));
        } else {
            return new SubsPlanViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_transaction_history_subs_plan, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        TxnDataBean bean = transactions.get(i);
        if(viewHolder instanceof TxnHistoryViewHolder) {
            TxnHistoryViewHolder holder = (TxnHistoryViewHolder) viewHolder;
            holder.packName_Txt.setText(bean.getPlanName());
            if (bean.getAmount() == 0.0) {
                holder.price_Txt.setVisibility(View.GONE);
            } else {
                holder.price_Txt.setVisibility(View.VISIBLE);
                holder.price_Txt.setText("" + bean.getNetamount() + " " + bean.getCurrency());
            }
            String Trxnstatus = bean.getTrxnstatus();
            if(Trxnstatus.equalsIgnoreCase("Failed")) {
                holder.status_Txt.setTextColor(ResUtil.getColor(holder.itemView.getResources(), R.color.red));
            } else {
                holder.status_Txt.setTextColor(ResUtil.getColor(holder.itemView.getResources(), R.color.green));
            }
            holder.status_Txt.setText(ResUtil.capitalizeFirstLetter(Trxnstatus));
            holder.paymentMode_Txt.setText(ResUtil.capitalizeFirstLetter(bean.getBillingmode()));
            holder.date_Txt.setText(bean.getTxnDate());
            if (bean.getTransactionid() == null || TextUtils.isEmpty(bean.getTransactionid())) {
                holder.orderId_Txt.setVisibility(View.GONE);
            } else {
                holder.orderId_Txt.setText(bean.getTransactionid());
                holder.orderId_Txt.setVisibility(View.VISIBLE);
            }
        }
        else if(viewHolder instanceof SubsPlanViewHolder) {
            SubsPlanViewHolder holder = (SubsPlanViewHolder) viewHolder;
            holder.packName_Txt.setText(bean.getPlanName());
            holder.date_Txt.setText("Start Date  "+bean.getsDate());
            holder.endDate_Txt.setText("End Date  "+(bean.geteDate() != null ? bean.geteDate() : ""));

            if(bean.getIsActive() == 1) {
                holder.status_Txt.setTextColor(ResUtil.getColor(holder.itemView.getResources(), R.color.green));
                holder.status_Txt.setText("Active");
            } else if(CommonUtil.isStartDateFutureDate(bean.getsDate())) {
                holder.status_Txt.setTextColor(ResUtil.getColor(holder.itemView.getResources(), R.color.black));
                holder.status_Txt.setText("Yet to active");
            } else{
                holder.status_Txt.setTextColor(ResUtil.getColor(holder.itemView.getResources(), R.color.red));
                holder.status_Txt.setText("Expired");
            }

        }

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }


    private static class TxnHistoryViewHolder extends RecyclerView.ViewHolder {

        TextView packName_Txt;
        TextView price_Txt;
        TextView status_Txt;
        TextView paymentMode_Txt;
        TextView date_Txt;
        TextView orderId_Txt;

        public TxnHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            packName_Txt = itemView.findViewById(R.id.planName_Txt);
            price_Txt = itemView.findViewById(R.id.price_Txt);
            status_Txt = itemView.findViewById(R.id.status_Txt);
            paymentMode_Txt = itemView.findViewById(R.id.paymentMode_Txt);
            date_Txt = itemView.findViewById(R.id.date_Txt);
            orderId_Txt = itemView.findViewById(R.id.orderId_Txt);

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



}
