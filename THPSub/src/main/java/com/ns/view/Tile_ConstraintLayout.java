package com.ns.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.netoperation.config.model.ColorOptionBean;
import com.netoperation.default_db.TableConfiguration;
import com.ns.activity.BaseAcitivityTHP;

public class Tile_ConstraintLayout extends ConstraintLayout {

    public Tile_ConstraintLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public Tile_ConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Tile_ConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
