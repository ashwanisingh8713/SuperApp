package com.ns.callbacks;

import com.netoperation.default_db.TableSection;
import com.netoperation.model.SectionBean;

/**
 * Created by arvind on 25/12/16.
 */

public interface OnExpandableListViewItemClickListener {
    void onExpandButtonClick(int groupPostion, boolean isExpanded);

    void onGroupClick(int groupPostion, TableSection tableSection, boolean isExpanded);

    void onChildClick(int groupPostion, int childPosition, TableSection groupSection, SectionBean childSection);
}
