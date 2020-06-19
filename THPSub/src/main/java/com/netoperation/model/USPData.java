package com.netoperation.model;

import java.util.List;

public class USPData {


    /**
     * STATUS : true
     * STATUS_MSG : Success
     * DATA : {"Dark":{"urls":["http://3.0.22.177/admin/assets/images/usp/th/Dark/xxhdpi/becomeAMember.png","http://3.0.22.177/admin/assets/images/usp/th/Dark/xxhdpi/briefing.png","http://3.0.22.177/admin/assets/images/usp/th/Dark/xxhdpi/myStories.png","http://3.0.22.177/admin/assets/images/usp/th/Dark/xxhdpi/personalisedRecommendation.png","http://3.0.22.177/admin/assets/images/usp/th/Dark/xxhdpi/adFree.png","http://3.0.22.177/admin/assets/images/usp/th/Dark/xxhdpi/fasterPage.png"]},"Light":{"urls":["http://3.0.22.177/admin/assets/images/usp/th/Light/xxhdpi/becomeAMember.png","http://3.0.22.177/admin/assets/images/usp/th/Light/xxhdpi/briefing.png","http://3.0.22.177/admin/assets/images/usp/th/Light/xxhdpi/myStories.png","http://3.0.22.177/admin/assets/images/usp/th/Light/xxhdpi/personalisedRecommendation.png","http://3.0.22.177/admin/assets/images/usp/th/Light/xxhdpi/adFree.png","http://3.0.22.177/admin/assets/images/usp/th/Light/xxhdpi/fasterPage.png"]}}
     */

    private boolean STATUS;
    private String STATUS_MSG;
    private DATABean DATA;

    public boolean isSTATUS() {
        return STATUS;
    }

    public void setSTATUS(boolean STATUS) {
        this.STATUS = STATUS;
    }

    public String getSTATUS_MSG() {
        return STATUS_MSG;
    }

    public void setSTATUS_MSG(String STATUS_MSG) {
        this.STATUS_MSG = STATUS_MSG;
    }

    public DATABean getDATA() {
        return DATA;
    }

    public void setDATA(DATABean DATA) {
        this.DATA = DATA;
    }

    public static class DATABean {
        /**
         * Dark : {"urls":["http://3.0.22.177/admin/assets/images/usp/th/Dark/xxhdpi/becomeAMember.png","http://3.0.22.177/admin/assets/images/usp/th/Dark/xxhdpi/briefing.png","http://3.0.22.177/admin/assets/images/usp/th/Dark/xxhdpi/myStories.png","http://3.0.22.177/admin/assets/images/usp/th/Dark/xxhdpi/personalisedRecommendation.png","http://3.0.22.177/admin/assets/images/usp/th/Dark/xxhdpi/adFree.png","http://3.0.22.177/admin/assets/images/usp/th/Dark/xxhdpi/fasterPage.png"]}
         * Light : {"urls":["http://3.0.22.177/admin/assets/images/usp/th/Light/xxhdpi/becomeAMember.png","http://3.0.22.177/admin/assets/images/usp/th/Light/xxhdpi/briefing.png","http://3.0.22.177/admin/assets/images/usp/th/Light/xxhdpi/myStories.png","http://3.0.22.177/admin/assets/images/usp/th/Light/xxhdpi/personalisedRecommendation.png","http://3.0.22.177/admin/assets/images/usp/th/Light/xxhdpi/adFree.png","http://3.0.22.177/admin/assets/images/usp/th/Light/xxhdpi/fasterPage.png"]}
         */

        private UrlkBean Dark;
        private UrlkBean Light;

        public UrlkBean getDark() {
            return Dark;
        }

        public void setDark(UrlkBean Dark) {
            this.Dark = Dark;
        }

        public UrlkBean getLight() {
            return Light;
        }

        public void setLight(UrlkBean Light) {
            this.Light = Light;
        }

        public static class UrlkBean {
            private List<String> urls;

            public List<String> getUrls() {
                return urls;
            }

            public void setUrls(List<String> urls) {
                this.urls = urls;
            }
        }


    }
}
