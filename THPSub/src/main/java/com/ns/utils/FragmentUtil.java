package com.ns.utils;


import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.netoperation.default_db.TableSection;
import com.netoperation.model.SectionBean;
import com.netoperation.net.DefaultTHApiManager;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class FragmentUtil {

    public static final int FRAGMENT_NO_ANIMATION = -1;
    public static final int FRAGMENT_ANIMATION = FragmentTransaction.TRANSIT_FRAGMENT_OPEN;

    public static void pushFragmentFromFragment(Fragment fragmentParent, int resId, Fragment fragment) {
        FragmentTransaction trasaction = fragmentParent.getChildFragmentManager().beginTransaction();
        trasaction.add(resId, fragment);
        trasaction.addToBackStack(null);
        trasaction.commit();
    }

    public static void replaceFragmentAnim(AppCompatActivity activity, int resId, Fragment fragment,
                                           final int animationSet, final boolean isRoot) {
        FragmentTransaction trasaction = activity.getSupportFragmentManager().beginTransaction();
        if (animationSet != FRAGMENT_NO_ANIMATION) {
            trasaction.setTransition(animationSet);
        }

        if (isRoot) {
            clearAllBackStack(activity);
            trasaction.replace(resId, fragment, activity.getSupportFragmentManager().getBackStackEntryCount() + "");
        } else {
            trasaction.replace(resId, fragment, activity.getSupportFragmentManager().getBackStackEntryCount() + "");
            trasaction.addToBackStack(null);
        }

        trasaction.commit();
    }

    public static void addFragmentAnim(AppCompatActivity activity, int resId, Fragment fragment,
                                        final int animationSet, final boolean isRoot) {
        FragmentTransaction trasaction = activity.getSupportFragmentManager().beginTransaction();
        if (animationSet != FRAGMENT_NO_ANIMATION) {
            trasaction.setTransition(animationSet);
        }

        if (isRoot) {
            clearAllBackStack(activity);
            trasaction.add(resId, fragment, activity.getSupportFragmentManager().getBackStackEntryCount() + "");
        } else {
            trasaction.add(resId, fragment, activity.getSupportFragmentManager().getBackStackEntryCount() + "");
            trasaction.addToBackStack(null);
        }

        trasaction.commit();
    }

    public static  void clearAllBackStack(AppCompatActivity activity) {
        activity.getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public static void clearSingleBackStack(AppCompatActivity activity) {
        activity.getSupportFragmentManager().popBackStack();
    }

    public static Disposable redirectionOnSectionAndSubSection(Context context, String secId) {
                return DefaultTHApiManager.isSectionOrSubsection(context, secId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(value->{
                            if(value instanceof SectionBean) {
                                // Sending Event in TopTabsFragment.java => handleEvent(SectionBean sectionBean)
                                EventBus.getDefault().post((SectionBean)value);
                            }
                            else if(value instanceof TableSection) {
                                // Sending Event in TopTabsFragment.java => handleEvent(TableSection tableSection)
                                EventBus.getDefault().post((TableSection)value);
                            }
                        });
    }


}
