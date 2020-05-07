package com.ns.model;

/**
 * Created by arvind on 14/1/16.
 */
public class GoldDollarDataModel implements SensexStatus {

    private int status = NONE;

    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataEntity {
        private String da;
        private String yd;

        private GoldsilverEntity goldsilver;

        private ForexEntity forex;

        public void setDa(String da) {
            this.da = da;
        }

        public void setYd(String yd) {
            this.yd = yd;
        }


        public void setGoldsilver(GoldsilverEntity goldsilver) {
            this.goldsilver = goldsilver;
        }

        public void setForex(ForexEntity forex) {
            this.forex = forex;
        }


        public String getDa() {
            return da;
        }

        public String getYd() {
            return yd;
        }

        public GoldsilverEntity getGoldsilver() {
            return goldsilver;
        }

        public ForexEntity getForex() {
            return forex;
        }

        public static class GoldsilverEntity {
            private String date;
            private String gold;
            private String silver;


            public void setDate(String date) {
                this.date = date;
            }

            public void setGold(String gold) {
                this.gold = gold;
            }

            public void setSilver(String silver) {
                this.silver = silver;
            }


            public String getDate() {
                return date;
            }

            public String getGold() {
                return gold;
            }

            public String getSilver() {
                return silver;
            }
        }

        public static class ForexEntity {
            private String date;
            private String cname;
            private String extime;
            private String ttsell;
            private String billsell;
            private String ttbuy;
            private String billbuy;


            public void setDate(String date) {
                this.date = date;
            }

            public void setCname(String cname) {
                this.cname = cname;
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


            public String getDate() {
                return date;
            }

            public String getCname() {
                return cname;
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
}


