package com.netoperation.asynctasks;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ns.activity.AppTabActivity;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.contentfragment.StockDetailsFragment;
import com.ns.model.CompanyData;
import com.ns.thpremium.R;

import java.util.List;

/**
 * Created by NS
 */

public class SearchAdapter extends BaseRecyclerViewAdapter {
    private Context mContext;
    private List<CompanyData> mSearchList;

    public SearchAdapter(Context mContext, List<CompanyData> _searchList) {
        this.mContext = mContext;
        mSearchList = _searchList;
    }

    @Override
    public SearchRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.search_stocks_recycler_item, parent, false);

        return new SearchRecyclerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderMain, final int position) {
        SearchRecyclerHolder holder = (SearchRecyclerHolder) holderMain;
        final CompanyData bean = mSearchList.get(position);
        holder.title.setText(bean.getName());
        holder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*CompanyData object = mSearchList.get(position);
                Intent i = new Intent(mContext, StockDetailsFragment.class);
                String nse = String.valueOf(object.getNse());
                String companyId = String.valueOf(object.getId());
                String companyName = object.getName();
                int bse = object.getBse();
                i.putExtra("tag", (bse == 0) ? 1 : (nse.equals(null)) ? 0 : "BSE");
                i.putExtra("bseCode", bse);
                i.putExtra("nseSymbol", nse);
                i.putExtra("companyId",companyId);
                i.putExtra("companyName", companyName);
                mContext.startActivity(i);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSearchList.size();
    }

    public void updateSearchedList(List<CompanyData> mList) {
        mSearchList = mList;
        notifyDataSetChanged();
    }

    public class SearchRecyclerHolder extends RecyclerView.ViewHolder {
        public View mParentLayout;
        public TextView title;

        public SearchRecyclerHolder(View itemView) {
            super(itemView);
            mParentLayout = itemView;
            title = itemView.findViewById(R.id.title);
        }
    }
}
