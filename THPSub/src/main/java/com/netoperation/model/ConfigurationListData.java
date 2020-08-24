package com.netoperation.model;

import java.util.List;

public class ConfigurationListData {


    /**
     * STATUS : true
     * STATUS_MSG : Success
     * DATA : {"totalRecords":4,"configurations":[{"ID":"1","NAME":"TH-Production"},{"ID":"3","NAME":"TH-DEFAULT"},{"ID":"5","NAME":"ASHWANI_TH"},{"ID":"6","NAME":"NABAJYOTI_TH"}]}
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
         * totalRecords : 4
         * configurations : [{"ID":"1","NAME":"TH-Production"},{"ID":"3","NAME":"TH-DEFAULT"},{"ID":"5","NAME":"ASHWANI_TH"},{"ID":"6","NAME":"NABAJYOTI_TH"}]
         */

        private int totalRecords;
        private List<ConfigurationsBean> configurations;

        public int getTotalRecords() {
            return totalRecords;
        }

        public void setTotalRecords(int totalRecords) {
            this.totalRecords = totalRecords;
        }

        public List<ConfigurationsBean> getConfigurations() {
            return configurations;
        }

        public void setConfigurations(List<ConfigurationsBean> configurations) {
            this.configurations = configurations;
        }

        public static class ConfigurationsBean {
            /**
             * ID : 1
             * NAME : TH-Production
             */

            private String ID;
            private String NAME;
            private boolean isSelected;

            public String getID() {
                return ID;
            }

            public void setID(String ID) {
                this.ID = ID;
            }

            public String getNAME() {
                return NAME;
            }

            public void setNAME(String NAME) {
                this.NAME = NAME;
            }

            public boolean isSelected() {
                return isSelected;
            }

            public void setSelected(boolean selected) {
                isSelected = selected;
            }
        }
    }
}
