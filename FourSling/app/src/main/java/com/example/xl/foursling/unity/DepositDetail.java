package com.example.xl.foursling.unity;

import java.util.List;

/**
 * Created by admin on 2016/12/26.
 * 提现明细
 */

public class DepositDetail {

    /**
     * code : 0
     * message : [{"tradingAmount":100,"createDate":1482660017000,"tradingOrderNO":"161225180017615074"}]
     */

    public int code;
    public List<MessageBean> message;


    public static class MessageBean {
        /**
         * tradingAmount : 100
         * createDate : 1482660017000
         * tradingOrderNO : 161225180017615074
         */

        public int tradingAmount;
        public long createDate;
        public String tradingOrderNO;

    }
}
