package com.ns.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.model.MyAddressModel;
import com.ns.thpremium.R;

import java.util.ArrayList;

public class MyAddressAdapter extends BaseRecyclerViewAdapter {

    private ArrayList<MyAddressModel> mAddressList;

    public MyAddressAdapter (ArrayList<MyAddressModel> addressList) {
        mAddressList = addressList;
    }

    @Override
    public int getItemViewType(int position) {
        return mAddressList.get(position).getViewType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_my_address, viewGroup, false);
        MyAddressHolder holder = new MyAddressHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return mAddressList.size();
    }

    private static class MyAddressHolder extends RecyclerView.ViewHolder{

        public MyAddressHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


}
