package com.netoperation.model;

public class ForceUpdateModel {

    private boolean STATUS;
    private String STATUS_MSG;
    private DATAModel DATA;

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

    public DATAModel getDATA() {
        return DATA;
    }

    public void setDATA(DATAModel DATA) {
        this.DATA = DATA;
    }

    public static class DATAModel {
        private UpdateModel android;
        private UpdateModel iOS;

        public UpdateModel getAndroid() {
            return android;
        }

        public void setAndroid(UpdateModel android) {
            this.android = android;
        }

        public UpdateModel getIOS() {
            return iOS;
        }

        public void setIOS(UpdateModel iOS) {
            this.iOS = iOS;
        }
    }
}
