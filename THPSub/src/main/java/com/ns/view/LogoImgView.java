package com.ns.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.netoperation.config.download.FileUtils;
import com.netoperation.config.model.ListingIconUrl;
import com.netoperation.config.model.OtherIconUrls;
import com.netoperation.default_db.TableConfiguration;
import com.netoperation.util.DefaultPref;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.thpremium.R;
import com.ns.utils.PicassoUtil;
import com.ns.utils.THPConstants;

public class LogoImgView extends AppCompatImageView {

    public LogoImgView(Context context) {
        super(context);
        init(context, null);
    }

    public LogoImgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LogoImgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        boolean isUserThemeDay = DefaultPref.getInstance(context).isUserThemeDay();

        if(attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NSImageLogo);
            int logoType = typedArray.getInt(R.styleable.NSImageLogo_logoType, 0);
            updateIcon(logoType);
        }





    }

    private void updateIcon(int logoType) {
        final boolean isUserThemeDay = DefaultPref.getInstance(getContext()).isUserThemeDay();
        TableConfiguration tableConfiguration = BaseAcitivityTHP.getTableConfiguration();
        if(tableConfiguration != null && THPConstants.IS_USE_SEVER_THEME) {
            String destinationFolderPath;
            if(isUserThemeDay) {
                // 0 = app:logoType="Splash"
                if(logoType == 0) {
                    destinationFolderPath = FileUtils.destinationFolder(getContext(), FileUtils.LOGOs_LIGHT).getPath();
                } else {
                    destinationFolderPath = FileUtils.destinationFolder(getContext(), FileUtils.TOPBAR_ICONs_LIGHT).getPath();
                }
                loadIconsFromServer(logoType, tableConfiguration.getOtherIconsDownloadUrls().getLight(), destinationFolderPath);
            } else {
                // 0 = app:logoType="Splash"
                if(logoType == 0) {
                    destinationFolderPath = FileUtils.destinationFolder(getContext(), FileUtils.LOGOs_DARK).getPath();
                } else {
                    destinationFolderPath = FileUtils.destinationFolder(getContext(), FileUtils.TOPBAR_ICONs_DARK).getPath();
                }
                loadIconsFromServer(logoType, tableConfiguration.getOtherIconsDownloadUrls().getDark(), destinationFolderPath);
            }
        }
        else {
            loadIconsFromApp(logoType, isUserThemeDay);
        }
    }

    private void loadIconsFromServer(int logoType, OtherIconUrls otherIconUrls, String destinationFolderPath) {
        String iconUrl = "";

        // 0 = app:logoType="Splash"
        if(logoType == 0) {
            iconUrl = otherIconUrls.getLogo();
        }
        // 1 = app:logoType="Section"
        else if(logoType == 1) {
            iconUrl = otherIconUrls.getTopbar().getLogo();
        }
        // 2 = app:logoType="ToolbarTHP"
        else if(logoType == 2) {
            iconUrl = otherIconUrls.getTopbar().getLogo();
        }

        if(iconUrl == null) {
            iconUrl = "";
        }

        PicassoUtil.loadImageFromCache(getContext(), this, FileUtils.getFilePathFromUrl(destinationFolderPath, iconUrl));
    }

    private void loadIconsFromApp(int logoType, boolean isUserThemeDay) {
        // 0 = app:logoType="Splash"
        if(logoType == 0) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.splash_logo);
            } else {
                setImageResource(R.drawable.splash_logo_dark);
            }
        }
        // 1 = app:logoType="Section"
        else if(logoType == 1) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.logo_actionbar);
            } else {
                setImageResource(R.drawable.logo_actionbar_dark);
            }
        }
        // 2 = app:logoType="ToolbarTHP"
        else if(logoType == 2) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.logo_actionbar);
            } else {
                setImageResource(R.drawable.logo_actionbar_dark);
            }
        }
    }

    public void setIcon(int logoType) {
        updateIcon(logoType);
    }


}
