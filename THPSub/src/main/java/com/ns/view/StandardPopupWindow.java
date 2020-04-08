package com.ns.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ns.thpremium.R;
import com.ns.utils.CommonUtil;

import java.util.List;


public class StandardPopupWindow extends android.widget.PopupWindow {
    private ListView mListView;
    private View popupView;
    private OnStandardPopupItemSelect mOnStandardPopupItemSelect;

    public interface OnStandardPopupItemSelect {
        void onStandardPopupItemSelect(String standard);

    }

    public void setOnStandardPopupItemSelect(OnStandardPopupItemSelect onStandardPopupItemSelect) {
        mOnStandardPopupItemSelect = onStandardPopupItemSelect;
    }

    public StandardPopupWindow(Context context, List<String> standardList, int widht, int height) {
        super(context);
        popupView = LayoutInflater.from(context).inflate(R.layout.popup_layout,null);
        setContentView(popupView);

        mListView = popupView.findViewById(R.id.listView);

        if(widht != -1) {
            setWidth(widht);
        } else {
            setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        }
        if(height != -1) {
            setHeight(height);
        } else {
            setHeight(CommonUtil.getDeviceHeight((Activity)context)/2);
        }

        // Closes the popup window when touch outside of it - when looses focus
        setOutsideTouchable(true);
        setFocusable(true);

        // Removes default black background
        setBackgroundDrawable(new BitmapDrawable());

        StandardAdapter adapter = new StandardAdapter(context, R.layout.item_standard_list_popup, standardList);
        mListView.setAdapter(adapter);



    } // End constructor

    // Attaches the view to its parent anchor-view at position x and y
    public void show(View anchor, int gravity, int x, int y) {
        showAtLocation(anchor, gravity, x, y);
    }


    private class StandardAdapter extends ArrayAdapter<String> {

        private List<String> mStandardList;
        private LayoutInflater inflater=null;

        public StandardAdapter(@NonNull Context context, int resource, List<String> standardList) {
            super(context, resource, standardList);
            mStandardList = standardList;
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            if(mStandardList == null) {
                return 0;
            }
            return mStandardList.size();
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View vi=convertView;
            if(convertView==null) {
                vi = inflater.inflate(R.layout.item_standard_list_popup, null);
            }

            TextView title = vi.findViewById(R.id.text);

            title.setText(getItem(position));

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnStandardPopupItemSelect != null) {
                        mOnStandardPopupItemSelect.onStandardPopupItemSelect(getItem(position));
                    }
                }
            });

            return vi;
        }


    }
}
