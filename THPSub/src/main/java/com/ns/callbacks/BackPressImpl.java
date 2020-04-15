package com.ns.callbacks;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class BackPressImpl {

    private Fragment parentFragment;
    private String mFrom;
    private int mTabIndex;

    public BackPressImpl() {

    }

    public BackPressImpl(Fragment parentFragment, String from, int tabIndex) {
        this.parentFragment = parentFragment;
        this.mFrom = from;
        this.mTabIndex = tabIndex;
    }

    public BackPressCallback onBackPressed() {


        if (parentFragment == null) {
            return new BackPressCallback(mFrom, false, mTabIndex);
        }

        int childCount = parentFragment.getChildFragmentManager().getBackStackEntryCount();

        if (childCount == 0) {
            // it has no child Fragment
            // can not handle the onBackPressed task by itself
            return new BackPressCallback(mFrom, false, mTabIndex);

        } else {
            // get the child Fragment
            FragmentManager childFragmentManager = parentFragment.getChildFragmentManager();
            // child Fragment was unable to handle the task
            // It could happen when the child Fragment is last last leaf of a chain
            // removing the child Fragment from stack
            childFragmentManager.popBackStackImmediate();

            // either this Fragment or its child handled the task
            // either way we are successful and done here
            return new BackPressCallback(mFrom, true, mTabIndex);
        }
    }
}
