package com.netoperation.model;

import java.util.List;

public class UserPlanList {


    /**
     * userPlanList : [{"planId":10,"planName":"30 days free trial","amount":0,"status":"success","nextRenewal":"2019-07-12","validity":"-","sDate":"2019-06-12","eDate":"2019-07-11","isActive":1},{"planId":39,"planName":"Value Pack - 6 Months","amount":11.49,"status":"addToCart","validity":"-","sDate":"2019-07-12","isActive":0},{"planId":40,"planName":"Readers Pack - 1 Year","amount":119,"status":"addToCart","validity":"-","sDate":"2019-07-12","isActive":0},{"planId":38,"planName":"Starter Pack - 1 Month","amount":119,"status":"addToCart","validity":"-","sDate":"2019-07-12","isActive":0},{"planId":38,"planName":"Starter Pack - 1 Month","amount":119,"status":"addToCart","validity":"-","sDate":"2019-07-12","isActive":0},{"planId":38,"planName":"Starter Pack - 1 Month","amount":119,"status":"addToCart","validity":"-","sDate":"2019-07-12","isActive":0},{"planId":39,"planName":"Value Pack - 6 Months","amount":299,"status":"addToCart","validity":"-","sDate":"2019-07-12","isActive":0},{"planId":40,"planName":"Readers Pack - 1 Year","amount":589,"status":"addToCart","validity":"-","sDate":"2019-07-12","isActive":0},{"planId":40,"planName":"Readers Pack - 1 Year","amount":589,"status":"addToCart","validity":"-","sDate":"2019-07-12","isActive":0}]
     * respCode : {"msg":"NONE","status":"Success","code":200}
     */

    private RespCodeBean respCode;
    private List<TxnDataBean> userPlanList;

    public RespCodeBean getRespCode() {
        return respCode;
    }

    public List<TxnDataBean> getUserPlanList() {
        return userPlanList;
    }


    public static class RespCodeBean {
        /**
         * msg : NONE
         * status : Success
         * code : 200
         */

        private String msg;
        private String status;
        private int code;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }


}
