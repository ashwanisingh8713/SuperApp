package com.ns.model;

import java.util.List;

/**
 * Created by arvind on 14/1/16.
 */
public class BSETopGainer {

    private String statuMessage;

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

    public static class Data {
        private String name;
        private String cp;
        private String prep;
        private String volume;
        private String date;
        private String change;
        private String per;

        public void setName(String name) {
            this.name = name;
        }

        public void setCp(String cp) {
            this.cp = cp;
        }

        public void setPrep(String prep) {
            this.prep = prep;
        }

        public void setVolume(String volume) {
            this.volume = volume;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setChange(String change) {
            this.change = change;
        }

        public void setPer(String per) {
            this.per = per;
        }

        public String getName() {
            return name;
        }

        public String getCp() {
            return cp;
        }

        public String getPrep() {
            return prep;
        }

        public String getVolume() {
            return volume;
        }

        public String getDate() {
            return date;
        }

        public String getChange() {
            return change;
        }

        public String getPer() {
            return per;
        }
    }
}
