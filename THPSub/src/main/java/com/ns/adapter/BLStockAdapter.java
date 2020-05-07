package com.ns.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by arvind on 14/1/16.
 */
public class BLStockAdapter extends ArrayAdapter<Object> {


    public BLStockAdapter(Context context, int resource) {
        super(context, resource);
    }

    public BLStockAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public BLStockAdapter(Context context, int resource, Object[] objects) {
        super(context, resource, objects);
    }

    public BLStockAdapter(Context context, int resource, int textViewResourceId, Object[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public BLStockAdapter(Context context, int resource, List<Object> objects) {
        super(context, resource, objects);
    }

    public BLStockAdapter(Context context, int resource, int textViewResourceId, List<Object> objects) {
        super(context, resource, textViewResourceId, objects);
    }


}
