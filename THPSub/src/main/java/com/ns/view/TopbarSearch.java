package com.ns.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.netoperation.config.model.ColorOptionBean;
import com.netoperation.default_db.TableConfiguration;
import com.netoperation.util.DefaultPref;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.thpremium.R;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;

public class TopbarSearch extends Toolbar {

    public TopbarSearch(Context context) {
        super(context);
        init(context);
    }

    public TopbarSearch(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TopbarSearch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.toolbar_for_search, this, true);
        boolean isUserThemeDay = DefaultPref.getInstance(context).isUserThemeDay();
        TableConfiguration tableConfiguration = BaseAcitivityTHP.getTableConfiguration();

        if(tableConfiguration != null && THPConstants.IS_USE_SEVER_THEME) {
            ColorOptionBean topbarTheme = tableConfiguration.getAppTheme().getTopBarBg();
            if(isUserThemeDay) {
                setBackgroundColor(Color.parseColor(topbarTheme.getLight()));
            } else {
                setBackgroundColor(Color.parseColor(topbarTheme.getDark()));
            }
        } else {
            if(isUserThemeDay) {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.topbar_light));
            }
            else {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.topbar_dark));
            }
        }
    }
}
