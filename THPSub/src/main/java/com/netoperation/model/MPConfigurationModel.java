package com.netoperation.model;

public class MPConfigurationModel {


    /**
     * STATUS : true
     * STATUS_MSG : Success
     * DATA : {"gmtInMillis":1583390290000,"configs":{"isTaboolaNeeded":true,"isMpBannerNeeded":true,"mpBannerMsg":"You read <readCount> articles out of <totalCount>","showFullAccessBtn":true,"fullAccessBtnName":"Full Access Button","showSignInBtn":true,"signInBtnName":"Already a user? Sign In","signInBtnNameBoldWord":"Sign In","showSignUpBtn":true,"signUpBtnName":"Sign Up Now and 30days free trial","signUpBtnNameBoldWord":"Sign Up","nonSignInBlockerTitle":"Title","nonSignInBlockerDescription":"Description","expiredUserBlockerTitle":"Title","expiredUserBlockerDescription":"Description"}}
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
         * gmtInMillis : 1583390290000
         * configs : {"isTaboolaNeeded":true,"isMpBannerNeeded":true,"mpBannerMsg":"You read <readCount> articles out of <totalCount>","showFullAccessBtn":true,"fullAccessBtnName":"Full Access Button","showSignInBtn":true,"signInBtnName":"Already a user? Sign In","signInBtnNameBoldWord":"Sign In","showSignUpBtn":true,"signUpBtnName":"Sign Up Now and 30days free trial","signUpBtnNameBoldWord":"Sign Up","nonSignInBlockerTitle":"Title","nonSignInBlockerDescription":"Description","expiredUserBlockerTitle":"Title","expiredUserBlockerDescription":"Description"}
         */

        private long gmtInMillis;
        private ConfigsBean configs;

        public long getGmtInMillis() {
            return gmtInMillis;
        }

        public void setGmtInMillis(long gmtInMillis) {
            this.gmtInMillis = gmtInMillis;
        }

        public ConfigsBean getConfigs() {
            return configs;
        }

        public void setConfigs(ConfigsBean configs) {
            this.configs = configs;
        }

        public static class ConfigsBean {
            /**
             * isTaboolaNeeded : true
             * isMpBannerNeeded : true
             * mpBannerMsg : You read <readCount> articles out of <totalCount>
             * showFullAccessBtn : true
             * fullAccessBtnName : Full Access Button
             * showSignInBtn : true
             * signInBtnName : Already a user? Sign In
             * signInBtnNameBoldWord : Sign In
             * showSignUpBtn : true
             * signUpBtnName : Sign Up Now and 30days free trial
             * signUpBtnNameBoldWord : Sign Up
             * nonSignInBlockerTitle : Title
             * nonSignInBlockerDescription : Description
             * expiredUserBlockerTitle : Title
             * expiredUserBlockerDescription : Description
             */

            private boolean isTaboolaNeeded;
            private boolean isMpBannerNeeded;
            private String mpBannerMsg;
            private boolean showFullAccessBtn;
            private String fullAccessBtnName;
            private boolean showSignInBtn;
            private String signInBtnName;
            private String signInBtnNameBoldWord;
            private boolean showSignUpBtn;
            private String signUpBtnName;
            private String signUpBtnNameBoldWord;
            private String nonSignInBlockerTitle;
            private String nonSignInBlockerDescription;
            private String expiredUserBlockerTitle;
            private String expiredUserBlockerDescription;

            public boolean isIsTaboolaNeeded() {
                return isTaboolaNeeded;
            }

            public void setIsTaboolaNeeded(boolean isTaboolaNeeded) {
                this.isTaboolaNeeded = isTaboolaNeeded;
            }

            public boolean isIsMpBannerNeeded() {
                return isMpBannerNeeded;
            }

            public void setIsMpBannerNeeded(boolean isMpBannerNeeded) {
                this.isMpBannerNeeded = isMpBannerNeeded;
            }

            public String getMpBannerMsg() {
                return mpBannerMsg;
            }

            public void setMpBannerMsg(String mpBannerMsg) {
                this.mpBannerMsg = mpBannerMsg;
            }

            public boolean isShowFullAccessBtn() {
                return showFullAccessBtn;
            }

            public void setShowFullAccessBtn(boolean showFullAccessBtn) {
                this.showFullAccessBtn = showFullAccessBtn;
            }

            public String getFullAccessBtnName() {
                return fullAccessBtnName;
            }

            public void setFullAccessBtnName(String fullAccessBtnName) {
                this.fullAccessBtnName = fullAccessBtnName;
            }

            public boolean isShowSignInBtn() {
                return showSignInBtn;
            }

            public void setShowSignInBtn(boolean showSignInBtn) {
                this.showSignInBtn = showSignInBtn;
            }

            public String getSignInBtnName() {
                return signInBtnName;
            }

            public void setSignInBtnName(String signInBtnName) {
                this.signInBtnName = signInBtnName;
            }

            public String getSignInBtnNameBoldWord() {
                return signInBtnNameBoldWord;
            }

            public void setSignInBtnNameBoldWord(String signInBtnNameBoldWord) {
                this.signInBtnNameBoldWord = signInBtnNameBoldWord;
            }

            public boolean isShowSignUpBtn() {
                return showSignUpBtn;
            }

            public void setShowSignUpBtn(boolean showSignUpBtn) {
                this.showSignUpBtn = showSignUpBtn;
            }

            public String getSignUpBtnName() {
                return signUpBtnName;
            }

            public void setSignUpBtnName(String signUpBtnName) {
                this.signUpBtnName = signUpBtnName;
            }

            public String getSignUpBtnNameBoldWord() {
                return signUpBtnNameBoldWord;
            }

            public void setSignUpBtnNameBoldWord(String signUpBtnNameBoldWord) {
                this.signUpBtnNameBoldWord = signUpBtnNameBoldWord;
            }

            public String getNonSignInBlockerTitle() {
                return nonSignInBlockerTitle;
            }

            public void setNonSignInBlockerTitle(String nonSignInBlockerTitle) {
                this.nonSignInBlockerTitle = nonSignInBlockerTitle;
            }

            public String getNonSignInBlockerDescription() {
                return nonSignInBlockerDescription;
            }

            public void setNonSignInBlockerDescription(String nonSignInBlockerDescription) {
                this.nonSignInBlockerDescription = nonSignInBlockerDescription;
            }

            public String getExpiredUserBlockerTitle() {
                return expiredUserBlockerTitle;
            }

            public void setExpiredUserBlockerTitle(String expiredUserBlockerTitle) {
                this.expiredUserBlockerTitle = expiredUserBlockerTitle;
            }

            public String getExpiredUserBlockerDescription() {
                return expiredUserBlockerDescription;
            }

            public void setExpiredUserBlockerDescription(String expiredUserBlockerDescription) {
                this.expiredUserBlockerDescription = expiredUserBlockerDescription;
            }
        }
    }
}
