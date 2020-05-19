package com.ns.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netoperation.default_db.TableSection;
import com.netoperation.model.SectionBean;
import com.netoperation.util.DefaultPref;
import com.ns.callbacks.OnExpandableListViewItemClickListener;
import com.ns.thpremium.R;
import com.ns.utils.ResUtil;

import java.util.List;

/**
 * Created by arvind on 24/12/16.
 */

public class NavigationExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<TableSection> mSectionList;
    private OnExpandableListViewItemClickListener onExpandableListViewItemClickListener;

    public NavigationExpandableListViewAdapter(Context mContext, List<TableSection> mSectionList, OnExpandableListViewItemClickListener onExpandableListViewItemClickListener) {
        this.mContext = mContext;
        this.mSectionList = mSectionList;
        this.onExpandableListViewItemClickListener = onExpandableListViewItemClickListener;
    }

    @Override
    public int getGroupCount() {
        return mSectionList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mSectionList.get(groupPosition).getSubSections().size();
    }

    @Override
    public TableSection getGroup(int groupPosition) {
        return mSectionList.get(groupPosition);
    }

    @Override
    public SectionBean getChild(int groupPosition, int childPosition) {
        return mSectionList.get(groupPosition).getSubSections().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder mGroupViewHolder = null;
        if (convertView == null) {
            mGroupViewHolder = new GroupViewHolder();
            convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                    inflate(R.layout.navigation_group_row, parent, false);
            mGroupViewHolder.mSectionNameLayout = convertView.findViewById(R.id.parantSectionNameLayout);
            mGroupViewHolder.textView = convertView.findViewById(R.id.title);
            mGroupViewHolder.mExpandButton = convertView.findViewById(R.id.expandButton);
            mGroupViewHolder.mNewTagImageView = convertView.findViewById(R.id.newTagImage);
            convertView.setTag(mGroupViewHolder);
        } else {
            mGroupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        if(getGroup(groupPosition) != null) {

            String mSectionName = getGroup(groupPosition).getSecName();
            String customeScreen = getGroup(groupPosition).getCustomScreen();
            if(mSectionName == null) {
                mSectionName = "";
            }
//            mGroupViewHolder.textView.setText(groupPosition+" :: "+mSectionName +" - "+customeScreen);
            mGroupViewHolder.textView.setText(mSectionName);

            //Set Epaper Text View color and logo
            String sectionIdEpaper = "159";
            if (getGroup(groupPosition).getSecId().equals(sectionIdEpaper)) {
                mGroupViewHolder.mNewTagImageView.setVisibility(View.VISIBLE);
                boolean isUserThemeDay = DefaultPref.getInstance(mGroupViewHolder.textView.getContext()).isUserThemeDay();
                if (isUserThemeDay) {
                    mGroupViewHolder.textView.setTextColor(ResUtil.getColor(mGroupViewHolder.textView.getContext().getResources(), R.color.epaper_day));
                    mGroupViewHolder.mNewTagImageView.setImageResource(R.drawable.ic_epaper_day_24x24);
                } else {
                    mGroupViewHolder.textView.setTextColor(ResUtil.getColor(mGroupViewHolder.textView.getContext().getResources(), R.color.color_ededed_dark));
                    mGroupViewHolder.mNewTagImageView.setImageResource(R.drawable.ic_epaper_night_24x24);
                }
            } else {
                //Reset Text Colors and Drawables
                mGroupViewHolder.mNewTagImageView.setVisibility(View.GONE);
                boolean isUserThemeDay = DefaultPref.getInstance(mGroupViewHolder.textView.getContext()).isUserThemeDay();
                if (isUserThemeDay) {
                    mGroupViewHolder.textView.setTextColor(ResUtil.getColor(mGroupViewHolder.textView.getContext().getResources(), R.color.color_191919_light));
                } else {
                    mGroupViewHolder.textView.setTextColor(ResUtil.getColor(mGroupViewHolder.textView.getContext().getResources(), R.color.color_ededed_dark));
                }
            }

            List<SectionBean> mChildList = getGroup(groupPosition).getSubSections();
            if (mChildList != null && mChildList.size() > 0) {
                mGroupViewHolder.mExpandButton.setVisibility(View.VISIBLE);
            } else {
                mGroupViewHolder.mExpandButton.setVisibility(View.INVISIBLE);
            }

            if (isExpanded) {
                mGroupViewHolder.mExpandButton.setBackgroundResource(R.drawable.minus_new);
            } else {
                mGroupViewHolder.mExpandButton.setBackgroundResource(R.drawable.ic_plus);
            }

            final GroupViewHolder finalMGroupViewHolder = mGroupViewHolder;
            mGroupViewHolder.mExpandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onExpandableListViewItemClickListener.onExpandButtonClick(groupPosition, isExpanded);
                    if (isExpanded) {
                        finalMGroupViewHolder.mExpandButton.setBackgroundResource(R.drawable.ic_plus);
                    } else {
                        finalMGroupViewHolder.mExpandButton.setBackgroundResource(R.drawable.minus_new);
                    }
                }
            });

            mGroupViewHolder.mSectionNameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onExpandableListViewItemClickListener.onGroupClick(groupPosition, getGroup(groupPosition), isExpanded);
                }
            });
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        if (convertView == null) {
            childViewHolder = new ChildViewHolder();
            convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.navigation_list_row, parent, false);
            childViewHolder.textView = convertView.findViewById(R.id.title);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.textView.setText(getChild(groupPosition, childPosition).getSecName());
        childViewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExpandableListViewItemClickListener.onChildClick(groupPosition, childPosition, getGroup(groupPosition), getChild(groupPosition, childPosition));
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class ChildViewHolder {
        public TextView textView;
    }

    public class GroupViewHolder {
        public LinearLayout mSectionNameLayout;
        public TextView textView;
        public Button mExpandButton;
        public ImageView mNewTagImageView;
    }


    public void addStaticItemGroup(List<TableSection> staticItemList) {
        mSectionList.addAll(staticItemList);
        notifyDataSetChanged();

    }
}
