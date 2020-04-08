package com.ns.userprofilefragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.netoperation.model.KeyValueModel;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.thpremium.R;
import com.ns.utils.FragmentUtil;
import com.ns.utils.ResUtil;

import java.util.ArrayList;
import java.util.List;

public class DropDownFragment extends BaseFragmentTHP {

    private String mFrom;

    private final String gender = "gender";
    private final String country = "country";
    private final String state = "state";

    private ArrayList<KeyValueModel> mList;

    @Override
    public int getLayoutRes() {
        if(mFrom != null && mFrom.equalsIgnoreCase(gender)) {
            return R.layout.fragment_dropdown;
        }
        else if(mFrom != null && mFrom.equalsIgnoreCase(country)) {
            return R.layout.fragment_dropdown_country;
        }
        else if(mFrom != null && mFrom.equalsIgnoreCase(state)) {
            return R.layout.fragment_dropdown_country;
        }
        else {
            return R.layout.fragment_dropdown;
        }
    }


    public static DropDownFragment getInstance(String from, ArrayList<KeyValueModel> dataList) {
        DropDownFragment fragment = new DropDownFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        bundle.putParcelableArrayList("data", dataList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            mFrom = getArguments().getString("from");
            mList = getArguments().getParcelableArrayList("data");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FrameLayout shadowLayout = view.findViewById(R.id.otpLayout);
        if(mIsDayTheme) {
            shadowLayout.setBackground(ResUtil.getBackgroundDrawable(getResources(), R.drawable.shadow_white_r12_s6_wh200_ltr));
        } else {
            shadowLayout.setBackground(ResUtil.getBackgroundDrawable(getResources(), R.drawable.shadow_dark_r12_s6_wh200_ltr));
        }

        ListView listView = view.findViewById(R.id.listView);

        view.findViewById(R.id.dropDownParentLayout).setOnTouchListener((v, e)->{
            FragmentUtil.clearSingleBackStack((AppCompatActivity)getActivity());
            return false;
        });

        KeyValueModel indiaModel = new KeyValueModel();
        indiaModel.setCode("IN");

        if(mList != null) {
            int index = mList.indexOf(indiaModel);
            if(index != -1) {
                KeyValueModel existingIndiaModel = mList.get(index);
                mList.remove(index);
                mList.add(0, existingIndiaModel);
            }
        }

        StandardAdapter adapter = new StandardAdapter(getActivity(), R.layout.item_standard_list_popup, mList);
        listView.setAdapter(adapter);



    }


    private class StandardAdapter extends ArrayAdapter<KeyValueModel> {

        private List<KeyValueModel> mStandardList;
        private LayoutInflater inflater=null;

        public StandardAdapter(@NonNull Context context, int resource, List<KeyValueModel> standardList) {
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

            TextView title = vi.findViewById(R.id.option_Txt);

            String name = getItem(position).getName();
            if(name == null || TextUtils.isEmpty(name)) {
                name = getItem(position).getState();
            }

            title.setText(name);

            title.setOnClickListener(v->{
                if(mOnDropdownItemSelection != null) {
                    mOnDropdownItemSelection.onDropdownItemSelection(mFrom, getItem(position));
                }
            });

            return vi;
        }


    }

    private OnDropdownItemSelection mOnDropdownItemSelection;

    public void setOnDropdownitemSelection(OnDropdownItemSelection onStandardPopupItemSelect) {
        mOnDropdownItemSelection = onStandardPopupItemSelect;
    }

    public interface OnDropdownItemSelection {
        void onDropdownItemSelection(String from, KeyValueModel model);
    }


}
