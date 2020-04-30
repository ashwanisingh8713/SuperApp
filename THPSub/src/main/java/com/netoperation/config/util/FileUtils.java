package com.netoperation.config.util;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static final String TAB_ICONs = "TAB";
    public static final String TOOLBAR_ICONs = "Toolbar";
    public static final String LISTING_ICONs = "Listing";
    public static final String LOGOs = "Logo";

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
