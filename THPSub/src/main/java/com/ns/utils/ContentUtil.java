package com.ns.utils;

import com.netoperation.model.MeBean;

import java.util.ArrayList;
import java.util.List;

public class ContentUtil {

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
