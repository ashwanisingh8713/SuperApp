package com.netoperation.model;

import java.util.List;

public class PlanRecoModel {


    /**
     * camapignList : [{"planId":"38","planName":"Starter Pack - 1 Month","amount":"119.0","validity":"1-month","thumbnailUrl":"http://alphath.thehindu.co.in","failUrl":"http://alphath.thehindu.co.in","successUrl":"http://alphath.thehindu.co.in","description":"IOS campaign","custLabel1":"IOS campaign","custLabel2":"IOS campaign","productId":"starterpacks"},{"planId":"40","planName":"Readers Pack - 1 Year","amount":"589.0","validity":"1-year","thumbnailUrl":"alphath.thehindu.co.in","failUrl":"alphath.thehindu.co.in","successUrl":"alphath.thehindu.co.in","description":"IOS Campaign","custLabel1":"IOS Campaign","custLabel2":"IOS Campaign","productId":"readerspacks"},{"planId":"39","planName":"Value Pack - 6 Months","amount":"299.0","validity":"6-month","thumbnailUrl":"alphath.thehindu.co.in","failUrl":"alphath.thehindu.co.in","successUrl":"alphath.thehindu.co.in","description":"IOS Campaign","custLabel1":"IOS Campaign","custLabel2":"IOS Campaign","productId":"valuepacks"}]
     * respCode : {"msg":"NONE","status":"Success","code":200}
     * country : India
     */

    private RespCodeBean respCode;
    private String country;
    private List<TxnDataBean> camapignList;

    public RespCodeBean getRespCode() {
        return respCode;
    }

    public void setRespCode(RespCodeBean respCode) {
        this.respCode = respCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<TxnDataBean> getCamapignList() {
        return camapignList;
    }

    public void setCamapignList(List<TxnDataBean> camapignList) {
        this.camapignList = camapignList;
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
