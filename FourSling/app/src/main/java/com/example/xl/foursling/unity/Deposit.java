package com.example.xl.foursling.unity;

import java.util.List;

/**
 * Created by admin on 2016/12/24.
 */

public class Deposit {

    /**
     * code : 0
     * message : [{"tradingAmount":11,"createDate":1482492717000},{"tradingAmount":10,"createDate":1482492717000}]
     */

    public int code;
    public List<MessageBean> message;


    public static class MessageBean {
        /**
         * tradingAmount : 11
         * createDate : 1482492717000
         */

        public int tradingAmount;
        public long createDate;
    }
}
