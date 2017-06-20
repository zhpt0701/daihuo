package com.example.xl.foursling.unity;

import java.util.List;

/**
 * Created by admin on 2016/12/26.
 */

public class Income {

    /**
     * code : 0
     * message : [{"payAmount":11,"payType":1,"createDate":1482734249000,"balance":112,"type_service":"3f515d78-6045-4419-b21f-e30f1effb0ba"},{"payAmount":12,"payType":1,"createDate":1482745852000,"balance":11,"type_service":"3f515d78-6045-4419-b21f-e30f1effb0ba"}]
     */

    public int code;
    public List<MessageBean> message;



    public static class MessageBean {
        /**
         * payAmount : 11
         * payType : 1
         * createDate : 1482734249000
         * balance : 112
         * type_service : 3f515d78-6045-4419-b21f-e30f1effb0ba
         */

        public int payAmount;
        public int payType;
        public long createDate;
        public int balance;
        public String type_service;


    }
}
