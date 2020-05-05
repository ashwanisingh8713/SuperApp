package com.netoperation.config.download;

import android.content.Context;
import android.os.Environment;

import androidx.core.util.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class FileUtils {

    public static final String TAB_ICONs_LIGHT = "TAB_LIGHT";
    public static final String TOPBAR_ICONs_LIGHT = "Topbar_LIGHT";
    public static final String LISTING_ICONs_LIGHT = "Listing_LIGHT";
    public static final String LOGOs_LIGHT = "Logo_LIGHT";
    public static final String PLACE_HOLDER_LIGHT = "PlaceHolder_LIGHT";

    public static final String TAB_ICONs_DARK = "TAB_DARK";
    public static final String TOPBAR_ICONs_DARK = "Topbar_DARK";
    public static final String LISTING_ICONs_DARK = "Listing_DARK";
    public static final String LOGOs_DARK = "Logo_DARK";
    public static final String PLACE_HOLDER_DARK = "PlaceHolder_DARK";

    public static void createFolderIfNeeded(@NonNull File folder) {
        if (!folder.exists() && !folder.mkdirs()) {
            //throw new RuntimeException("Can't create directory");
        }
    }

    public static void deleteFolderIfNeeded(@NonNull File folder) {
        if (folder.exists() && !folder.delete()) {
            //throw new RuntimeException("Can't delete folder");
        }

    }

    public static void removeDuplicateFileIfExist(@NonNull File folder, @NonNull String fileName) {
        File file = new File(folder, fileName);
        if (file.exists() && !file.delete()) {
            //throw new RuntimeException("Can't delete file");
        }
    }


    public static File destinationFolder( boolean inPublicDir, Context context, String destinationPath) {
        return inPublicDir
                ? Environment.getExternalStoragePublicDirectory(destinationPath)
                : new File(context.getFilesDir(), destinationPath);
    }

    public static File destinationFolder(Context context, String destinationPath) {
        return new File(context.getFilesDir(), destinationPath);
    }

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

    public static Pair<Integer, List<String>> getAllIconsCount(Context context) {

        int urlCount = 0;
        List<String> missingIconFolder = new ArrayList<>();

        File folder = destinationFolder(false, context, TAB_ICONs_LIGHT);
        if(folder != null && folder.list() != null && folder.list().length > 0) {
            urlCount +=folder.list().length;
        }
        else {
            missingIconFolder.add(TAB_ICONs_LIGHT);
        }

        folder = destinationFolder(false, context, TOPBAR_ICONs_LIGHT);
        if(folder != null && folder.list() != null && folder.list().length > 0) {
            urlCount +=folder.list().length;
        }
        else {
            missingIconFolder.add(TOPBAR_ICONs_LIGHT);
        }

        folder = destinationFolder(false, context, LISTING_ICONs_LIGHT);
        if(folder != null && folder.list() != null && folder.list().length > 0) {
            urlCount +=folder.list().length;
        }
        else {
            missingIconFolder.add(LISTING_ICONs_LIGHT);
        }

        folder = destinationFolder(false, context, LOGOs_LIGHT);
        if(folder != null && folder.list() != null && folder.list().length > 0) {
            urlCount +=folder.list().length;
        }
        else {
            missingIconFolder.add(LOGOs_LIGHT);
        }

        folder = destinationFolder(false, context, PLACE_HOLDER_LIGHT);
        if(folder != null && folder.list() != null && folder.list().length > 0) {
            urlCount +=folder.list().length;
        }
        else {
            missingIconFolder.add(PLACE_HOLDER_LIGHT);
        }

        folder = destinationFolder(false, context, TAB_ICONs_DARK);
        if(folder != null && folder.list() != null && folder.list().length > 0) {
            urlCount +=folder.list().length;
        }
        else {
            missingIconFolder.add(TAB_ICONs_DARK);
        }

        folder = destinationFolder(false, context, TOPBAR_ICONs_DARK);
        if(folder != null && folder.list() != null && folder.list().length > 0) {
            urlCount +=folder.list().length;
        }
        else {
            missingIconFolder.add(TOPBAR_ICONs_DARK);
        }

        folder = destinationFolder(false, context, LISTING_ICONs_DARK);
        if(folder != null && folder.list() != null && folder.list().length > 0) {
            urlCount +=folder.list().length;
        }
        else {
            missingIconFolder.add(LISTING_ICONs_DARK);
        }

        folder = destinationFolder(false, context, LOGOs_DARK);
        if(folder != null && folder.list() != null && folder.list().length > 0) {
            urlCount +=folder.list().length;
        }
        else {
            missingIconFolder.add(LOGOs_DARK);
        }

        folder = destinationFolder(false, context, PLACE_HOLDER_DARK);
        if(folder != null && folder.list() != null && folder.list().length > 0) {
            urlCount +=folder.list().length;
        }
        else {
            missingIconFolder.add(PLACE_HOLDER_DARK);
        }


        return new Pair<>(urlCount, missingIconFolder);
    }


}