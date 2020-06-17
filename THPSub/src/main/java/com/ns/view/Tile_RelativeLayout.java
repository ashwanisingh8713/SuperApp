package com.ns.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.netoperation.config.model.ColorOptionBean;
import com.netoperation.default_db.TableConfiguration;
import com.ns.activity.BaseAcitivityTHP;

public class Tile_RelativeLayout extends RelativeLayout {

    public Tile_RelativeLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public Tile_RelativeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Tile_RelativeLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        TableConfiguration tableConfiguration = BaseAcitivityTHP.getTableConfiguration();
        if(tableConfiguration != null) {
            ColorOptionBean colorOptionBean = tableConfiguration.getAppTheme().getTilesBg();
            if(BaseAcitivityTHP.sIsDayTheme) {
                setBackgroundColor(Color.parseColor(colorOptionBean.getLight()));
            }
            else {
                setBackgroundColor(Color.parseColor(colorOptionBean.getDark()));
            }
        }
    }
}
