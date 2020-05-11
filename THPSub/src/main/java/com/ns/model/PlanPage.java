package com.ns.model;

public class PlanPage {
    /**
     * status : true
     * data : {"url":"https://myaccount.thehindu.co.in/androidwebview","url_ios":"http://hindumysqlstaging.ninestars.in/webview","plans":{"P1":"https://myaccount.thehindu.co.in/androidweb10percent?test","P2":"https://myaccount.thehindu.co.in/androidweb20percent?test","P3":"https://myaccount.thehindu.co.in/androidweb25percent?test"}}
     */

    private boolean status;
    private DataBean data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * url : https://myaccount.thehindu.co.in/androidwebview
         * url_ios : http://hindumysqlstaging.ninestars.in/webview
         * plans : {"P1":"https://myaccount.thehindu.co.in/androidweb10percent?test","P2":"https://myaccount.thehindu.co.in/androidweb20percent?test","P3":"https://myaccount.thehindu.co.in/androidweb25percent?test"}
         */

        private String url;
        private String url_ios;
        private PlansBean plans;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl_ios() {
            return url_ios;
        }

        public void setUrl_ios(String url_ios) {
            this.url_ios = url_ios;
        }

        public PlansBean getPlans() {
            return plans;
        }

        public void setPlans(PlansBean plans) {
            this.plans = plans;
        }

        public static class PlansBean {
            /**
             * P1 : https://myaccount.thehindu.co.in/androidweb10percent?test
             * P2 : https://myaccount.thehindu.co.in/androidweb20percent?test
             * P3 : https://myaccount.thehindu.co.in/androidweb25percent?test
             */

            private String P1;
            private String P2;
            private String P3;

            public String getP1() {
                return P1;
            }

            public void setP1(String P1) {
                this.P1 = P1;
            }

            public String getP2() {
                return P2;
            }

            public void setP2(String P2) {
                this.P2 = P2;
            }

            public String getP3() {
                return P3;
            }

            public void setP3(String P3) {
                this.P3 = P3;
            }
        }
    }
}
