package com.example.xl.foursling.unity;

import java.util.List;

/**
 * Created by admin on 2016/12/26.
 * 充值详情
 */

public class PrepaidDetails {

    /**
     * code : 0
     * message : [{"payAmount":11,"payType":1,"createDate":1482734249000,"balance":112}]
     */

    public int code;
    public List<MessageBean> message;


    public static class MessageBean {
        /**
         * payAmount : 11
         * payType : 1
         * createDate : 1482734249000
         * balance : 112
         */

        public int payAmount;
        public int payType;
        public long createDate;
        public int balance;

    }
}
