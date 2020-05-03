package com.netoperation.config.download;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static final String TAB_ICONs_LIGHT = "TAB_LIGHT";
    public static final String TOPBAR_ICONs_LIGHT = "Topbar_LIGHT";
    public static final String LISTING_ICONs_LIGHT = "Listing_LIGHT";
    public static final String LOGOs_LIGHT = "Logo_LIGHT";

    public static final String TAB_ICONs_DARK = "TAB_DARK";
    public static final String TOPBAR_ICONs_DARK = "Topbar_DARK";
    public static final String LISTING_ICONs_DARK = "Listing_DARK";
    public static final String LOGOs_DARK = "Logo_DARK";

    public static String getFileNameFromUrl(String url) {
        try {
            return url.substring(url.lastIndexOf("/") + 1, url.length());
        } catch (Exception e) {
            return url;
        }
    }

    public static File getImageFileInput(File directory, String imgUrl) throws IOException {
        final String fileName = getFileNameFromUrl(imgUrl);
        File imageFile = new File(directory, fileName);
        return imageFile;
    }


}
