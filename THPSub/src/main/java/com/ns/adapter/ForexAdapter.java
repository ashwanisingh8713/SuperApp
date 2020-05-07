/*
 * Copyright (c) 2014 Mobstac TM
 * All Rights Reserved.
 * @since Nov 21, 2014 
 * @author rajeshcp
 */
package com.ns.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.netoperation.util.DefaultPref;
import com.ns.model.ForexData;
import com.ns.thpremium.R;

import java.util.List;


public class ForexAdapter extends BaseAdapter {

    private Context context;
    private List<ForexData.Data> forexDatas;

    public ForexAdapter(Context context, List<ForexData.Data> forexDatas) {
        this.context = context;
        this.forexDatas = forexDatas;
    }

    @Override
    public int getCount() {
        return forexDatas.size();
    }

    @Override
    public ForexData.Data getItem(int i) {
        return forexDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.forex_row, parent, false);
        ((TextView) convertView.findViewById(R.id.currency_row_value)).setText(getItem(position).getCname());
        ((TextView) convertView.findViewById(R.id.tt_selling_row_value)).setText(getItem(position).getTtsell());
        ((TextView) convertView.findViewById(R.id.tt_buying_row_value)).setText(getItem(position).getTtbuy());
        ((TextView) convertView.findViewById(R.id.bill_selling_row_value)).setText(getItem(position).getBillsell());
        ((TextView) convertView.findViewById(R.id.bill_buying_row_value)).setText(getItem(position).getBillbuy());

        boolean isUserThemeDay = DefaultPref.getInstance(context).isUserThemeDay();

        if (position % 2 == 0) {
            if(isUserThemeDay) {
                convertView.setBackgroundResource(R.color.forex_row_background);
            } else {
                convertView.setBackgroundResource(R.color.forex_row_background_dark);
            }
        } else {
            if(isUserThemeDay) {
                convertView.setBackgroundResource(R.color.forex_row_background_second);
            } else {
                convertView.setBackgroundResource(R.color.forex_row_background_second_dark);
            }
        }
        return convertView;
    }

}
