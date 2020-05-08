package com.ns.model;

import java.util.List;

/**
 * Created by arvind on 18/1/16.
 */
public class ForexData {

    private String statuMessage;

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    private String lastUpdatedDate;
    private List<Data> data;

    public void setStatuMessage(String statuMessage) {
        this.statuMessage = statuMessage;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getStatuMessage() {
        return statuMessage;
    }

    public List<Data> getData() {
        return data;
    }

    public class Data {
        private String cname;
        private String date;
        private String extime;
        private String ttsell;
        private String billsell;
        private String ttbuy;
        private String billbuy;

        public void setCname(String cname) {
            this.cname = cname;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setExtime(String extime) {
            this.extime = extime;
        }

        public void setTtsell(String ttsell) {
            this.ttsell = ttsell;
        }

        public void setBillsell(String billsell) {
            this.billsell = billsell;
        }

        public void setTtbuy(String ttbuy) {
            this.ttbuy = ttbuy;
        }

        public void setBillbuy(String billbuy) {
            this.billbuy = billbuy;
        }

        public String getCname() {
            return cname;
        }

        public String getDate() {
            return date;
        }

        public String getExtime() {
            return extime;
        }

        public String getTtsell() {
            return ttsell;
        }

        public String getBillsell() {
            return billsell;
        }

        public String getTtbuy() {
            return ttbuy;
        }

        public String getBillbuy() {
            return billbuy;
        }
    }
}
