package com.netoperation.model;

public class MPCycleDurationModel {


    /**
     * STATUS : true
     * STATUS_MSG : Success
     * DATA : {"cycleName":"THMPC1","expiryInSeconds":60,"numOfAllowedArticles":"5","uniqueId":"f594166a797b0de4d92c48207ccf6d80"}
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
         * cycleName : THMPC1
         * expiryInSeconds : 60
         * numOfAllowedArticles : 5
         * uniqueId : f594166a797b0de4d92c48207ccf6d80
         */

        private String cycleName;
        private long expiryInSeconds;
        private long gmtInMillis;
        private int numOfAllowedArticles;
        private String uniqueId;


        public String getCycleName() {
            return cycleName;
        }

        public void setCycleName(String cycleName) {
            this.cycleName = cycleName;
        }

        public long getExpiryInSeconds() {
            return expiryInSeconds;
        }



        public int getNumOfAllowedArticles() {
            return numOfAllowedArticles;
        }

        public void setNumOfAllowedArticles(int numOfAllowedArticles) {
            this.numOfAllowedArticles = numOfAllowedArticles;
        }

        public String getUniqueId() {
            return uniqueId;
        }

        public void setUniqueId(String uniqueId) {
            this.uniqueId = uniqueId;
        }

        public long getGmtInMillis() {
            return gmtInMillis;
        }
    }
}
