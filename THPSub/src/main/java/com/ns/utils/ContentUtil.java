package com.ns.utils;

import com.main.SuperApp;
import com.netoperation.model.MeBean;
import com.netoperation.util.DefaultPref;
import com.netoperation.util.NetConstants;
import com.netoperation.util.PremiumPref;
import com.ns.callbacks.ToolbarChangeRequired;

import java.util.ArrayList;
import java.util.List;

public class ContentUtil {


    public static ToolbarChangeRequired getTopbarFromGroupType(String groupType) {
        final ToolbarChangeRequired toolbarChangeRequired = new ToolbarChangeRequired();
        if(PremiumPref.getInstance(SuperApp.getAppContext()).isHasSubscription()) {

            if(groupType == null) {
                toolbarChangeRequired.setTypeOfToolbar(ToolbarChangeRequired.DEFAULT_DETAIL_TOPBAR);
            }
            else if(groupType.equals(NetConstants.G_BOOKMARK_DEFAULT)) {
                toolbarChangeRequired.setTypeOfToolbar(ToolbarChangeRequired.DEFAULT_DETAIL_TOPBAR);
            }
            else if(groupType.equals(NetConstants.G_BOOKMARK_PREMIUM)) {
                toolbarChangeRequired.setTypeOfToolbar(ToolbarChangeRequired.PREMIUM_DETAIL_TOPBAR);
            }

        }
        else {
            if(groupType == null) {
                toolbarChangeRequired.setTypeOfToolbar(ToolbarChangeRequired.DEFAULT_DETAIL_TOPBAR_CROWN);
            }
            else if(groupType.equals(NetConstants.G_BOOKMARK_DEFAULT)) {
                toolbarChangeRequired.setTypeOfToolbar(ToolbarChangeRequired.DEFAULT_DETAIL_TOPBAR_CROWN);
            }
            else if(groupType.equals(NetConstants.G_BOOKMARK_PREMIUM)) {
                toolbarChangeRequired.setTypeOfToolbar(ToolbarChangeRequired.PREMIUM_DETAIL_TOPBAR_CROWN);
            }
        }

        return toolbarChangeRequired;
    }

    /*
     * Check shall MP should show
     * */
    public static boolean shouldShowMeteredPaywall() {
        if (DefaultPref.getInstance(SuperApp.getAppContext()).isMeteredPaywallEnabled()) {
            if (PremiumPref.getInstance(SuperApp.getAppContext()).isUserLoggedIn() && !PremiumPref.getInstance(SuperApp.getAppContext()).isUserAdsFree())
                return true;
            else if (PremiumPref.getInstance(SuperApp.getAppContext()).isUserLoggedIn() && PremiumPref.getInstance(SuperApp.getAppContext()).isUserAdsFree())
                return false;
            else if (!PremiumPref.getInstance(SuperApp.getAppContext()).isUserLoggedIn())
                return true;
            else
                return true;
        } else {
            return false;
        }
    }

    public static boolean isFromBookmarkPage(String from) {
        if(from != null && (from.equalsIgnoreCase(NetConstants.BOOKMARK_IN_ONE)
                || from.equalsIgnoreCase(NetConstants.BOOKMARK_IN_TAB)
                || from.equalsIgnoreCase(NetConstants.G_BOOKMARK_PREMIUM)
                || from.equalsIgnoreCase(NetConstants.G_BOOKMARK_DEFAULT)
                || from.equalsIgnoreCase(NetConstants.API_bookmarks))) {
            return true;
        }
        return false;
    }

    public static boolean isFromPremiumBookmark(String from) {
        if(from != null && (from.equalsIgnoreCase(NetConstants.BOOKMARK_IN_ONE)
                || from.equalsIgnoreCase(NetConstants.BOOKMARK_IN_TAB)
                || from.equalsIgnoreCase(NetConstants.G_BOOKMARK_PREMIUM))) {
            return true;
        }
        return false;
    }

    public static boolean isFromNonPremiumBookmark(String from) {
        if(from != null && (from.equalsIgnoreCase(NetConstants.BOOKMARK_IN_ONE)
                || from.equalsIgnoreCase(NetConstants.BOOKMARK_IN_TAB)
                || from.equalsIgnoreCase(NetConstants.G_BOOKMARK_DEFAULT))) {
            return true;
        }
        return false;
    }

    public static StringBuilder getAuthor(List<String> authors) {
        StringBuilder author = new StringBuilder();
        if(authors != null && authors.size()>0) {
            int authorSize = authors.size();
            int count = 1;
            for (String str : authors) {
                if(authorSize > 1 && count < authorSize) {
                    author.append(str+ ", ");
                } else if(authorSize == count) {
                    author.append(str);
                }
                count++;
            }

        }
        return author;
    }

    public static String getBannerUrl(ArrayList<MeBean> meBeans, ArrayList<MeBean> briefingMeBeans, List<String> thumbUrls) {
        if(meBeans != null && meBeans.size()>0) {
            return meBeans.get(0).getIm_v2();
        }
        else if(briefingMeBeans != null && briefingMeBeans.size()>0) {
            return briefingMeBeans.get(0).getImage();
        }
        else if(thumbUrls != null && thumbUrls.size()>0) {
            return thumbUrls.get(0);
        }
        return "";
    }

    private static final String THUMB_SIZE ="SQUARE_170";
    private static final String CARTOON_SIZE ="LANDSCAPE_435";
    private static final String BANNER_SIZE ="LANDSCAPE_435";
    private static final String WIDGET_SIZE ="LANDSCAPE_240";
    private static final String MULTIMEDIA_SIZE ="LANDSCAPE_240";





    public static String getThumbUrl(List<String> urls) {
        if(urls != null && urls.size()>0) {
            String imageUrl = urls.get(0);
            if(imageUrl != null && imageUrl.contains("BINARY")) {
                imageUrl = urls.get(0).replace("BINARY/thumbnail", "alternates/SQUARE_170");
            } else if(imageUrl != null) {
                imageUrl = urls.get(0).replace("FREE_660", THUMB_SIZE);
            }
            return imageUrl;
        }
        return "http://";
    }

    public static String getThumbUrl(String imageUrl) {
            if(imageUrl != null && imageUrl.contains("BINARY")) {
                imageUrl = imageUrl.replace("BINARY/thumbnail", "alternates/SQUARE_170");
            } else if(imageUrl != null) {
                imageUrl = imageUrl.replace("FREE_660", THUMB_SIZE);
            }
            return imageUrl;
    }

    public static String getBreifingImgUrl(List<String> urls) {
        if(urls != null && urls.size()>0) {
            String imageUrl = urls.get(0);
            if(imageUrl != null && imageUrl.contains("BINARY")) {
                imageUrl = urls.get(0).replace("BINARY/thumbnail", "alternates/LANDSCAPE_435");
            } else if(imageUrl != null) {
                imageUrl = urls.get(0).replace("FREE_660", BANNER_SIZE);
            }
            return imageUrl;
        }
        return "http://";
    }

    public static String getBannerUrl(List<String> urls) {
        if(urls != null && urls.size()>0) {
            String imageUrl = urls.get(0);
            if (imageUrl != null) {
                if (imageUrl != null && imageUrl.contains("BINARY")) {
                    imageUrl = imageUrl.replace("BINARY/thumbnail", "alternates/LANDSCAPE_435");
                } else if (imageUrl != null) {
                    imageUrl = imageUrl.replace("FREE_660", BANNER_SIZE);
                }
                return imageUrl;
            }
        }
        return "";
    }

    public static String getBannerUrl(String imageUrl) {
        if (imageUrl != null) {
            if (imageUrl != null && imageUrl.contains("BINARY")) {
                imageUrl = imageUrl.replace("BINARY/thumbnail", "alternates/LANDSCAPE_435");
            } else if (imageUrl != null) {
                imageUrl = imageUrl.replace("FREE_660", BANNER_SIZE);
            }
            return imageUrl;
        }

        return "";
    }

    public static String getWidgetUrl(String imageUrl) {
        if (imageUrl != null) {
            if (imageUrl != null && imageUrl.contains("BINARY")) {
                imageUrl = imageUrl.replace("BINARY/thumbnail", "alternates/LANDSCAPE_435");
            } else if (imageUrl != null) {
                imageUrl = imageUrl.replace("FREE_660", WIDGET_SIZE);
            }
            return imageUrl;
        }

        return "";
    }

    public static String getCartoonUrl(String imageUrl) {
        if (imageUrl != null) {
            if (imageUrl != null && imageUrl.contains("BINARY")) {
                imageUrl = imageUrl.replace("BINARY/thumbnail", "alternates/LANDSCAPE_435");
            } else if (imageUrl != null) {
                imageUrl = imageUrl.replace("FREE_660", CARTOON_SIZE);
            }
            return imageUrl;
        }

        return "";
    }

    public static String getMultimediaUrl(String imageUrl) {
        if (imageUrl != null) {
            if (imageUrl != null && imageUrl.contains("BINARY")) {
                imageUrl = imageUrl.replace("BINARY/thumbnail", "alternates/LANDSCAPE_435");
            } else if (imageUrl != null) {
                imageUrl = imageUrl.replace("FREE_660", MULTIMEDIA_SIZE);
            }
            return imageUrl;
        }

        return "";
    }
}
