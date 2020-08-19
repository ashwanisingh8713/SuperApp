package com.netoperation.model;

public class ConfigUpdateCheck {
    /**
     * STATUS : true
     * STATUS_MSG : Success
     * DATA : {"lastUpdatedTime":1594116440000}
     */

    private boolean STATUS;
    private String STATUS_MSG;
    private ConfigDATABean DATA;

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

    public ConfigDATABean getDATA() {
        return DATA;
    }

    public void setDATA(ConfigDATABean DATA) {
        this.DATA = DATA;
    }

    public static class ConfigDATABean {
        /**
         * lastUpdatedTime : 1594116440000
         */

        private String lastUpdatedTime;

        public String getLastUpdatedTime() {
            return lastUpdatedTime;
        }

        public void setLastUpdatedTime(String lastUpdatedTime) {
            this.lastUpdatedTime = lastUpdatedTime;
        }
    }
}
