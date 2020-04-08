package com.netoperation.model;

import java.util.List;

public class TransactionHistoryModel {


    /**
     * txnData : [{"userId":"80","planId":"8","planName":"Value pack","transactionid":"20190123173729gIM","trxnstatus":"add to cart","isactive":0,"amount":499,"netamount":0,"currency":"INR","billingmode":"paytm","validity":"1-year","igst":0,"igstamt":0,"cgst":0,"cgstamt":0,"sgst":0,"sgstamt":0,"utgst":0,"utgstamt":0,"txnDate":"2019-01-23 17:37:29","payMode":"","planType":"Value pack"},{"userId":"80","planId":"8","planName":"Value pack","transactionid":"20190123150243F1a","trxnstatus":"add to cart","isactive":0,"amount":499,"netamount":0,"currency":"INR","billingmode":"paytm","validity":"1-year","igst":0,"igstamt":0,"cgst":0,"cgstamt":0,"sgst":0,"sgstamt":0,"utgst":0,"utgstamt":0,"txnDate":"2019-01-23 15:02:43","payMode":"","planType":"Value pack"},{"userId":"80","planId":"8","planName":"Value pack","transactionid":"20190123145822Hqi","trxnstatus":"add to cart","isactive":0,"amount":499,"netamount":0,"currency":"INR","billingmode":"paytm","validity":"1-year","igst":0,"igstamt":0,"cgst":0,"cgstamt":0,"sgst":0,"sgstamt":0,"utgst":0,"utgstamt":0,"txnDate":"2019-01-23 14:58:21","payMode":"","planType":"Value pack"},{"userId":"80","planId":"7","planName":"Starter Pack","transactionid":"20190123145540R79","trxnstatus":"add to cart","isactive":0,"amount":99,"netamount":0,"currency":"INR","billingmode":"paytm","validity":"1-month","igst":0,"igstamt":0,"cgst":0,"cgstamt":0,"sgst":0,"sgstamt":0,"utgst":0,"utgstamt":0,"txnDate":"2019-01-23 14:55:40","payMode":"","planType":"Starter Plan"},{"userId":"80","planId":"9","planName":"Reader's pack","transactionid":"201901222038371mb","trxnstatus":"add to cart","isactive":0,"amount":899,"netamount":0,"currency":"INR","billingmode":"paytm","validity":"2-year","igst":0,"igstamt":0,"cgst":0,"cgstamt":0,"sgst":0,"sgstamt":0,"utgst":0,"utgstamt":0,"txnDate":"2019-01-22 20:38:37","payMode":"","planType":"Readers pack"},{"userId":"80","planId":"8","planName":"Value pack","transactionid":"201901222035110eB","trxnstatus":"add to cart","isactive":0,"amount":499,"netamount":0,"currency":"INR","billingmode":"paytm","validity":"1-year","igst":0,"igstamt":0,"cgst":0,"cgstamt":0,"sgst":0,"sgstamt":0,"utgst":0,"utgstamt":0,"txnDate":"2019-01-22 20:35:11","payMode":"","planType":"Value pack"}]
     * txnCount : 6
     * respCode : {"msg":"NONE","status":"Success","code":200}
     */

    private String txnCount;
    private RespCodeBean respCode;
    private List<TxnDataBean> txnData;

    public String getTxnCount() {
        return txnCount;
    }

    public void setTxnCount(String txnCount) {
        this.txnCount = txnCount;
    }

    public RespCodeBean getRespCode() {
        return respCode;
    }

    public void setRespCode(RespCodeBean respCode) {
        this.respCode = respCode;
    }

    public List<TxnDataBean> getTxnData() {
        return txnData;
    }

    public void setTxnData(List<TxnDataBean> txnData) {
        this.txnData = txnData;
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
