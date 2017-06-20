package com.example.xl.foursling.unity;

import java.util.List;

/**
 * Created by admin on 2016/12/26.
 */

public class ShowDetail {

    /**
     * code : 0
     * message : [{"payAmount":1200,"amount":60,"userPayNO":"15029971182","createDate":1482722616000,"balance":0}]
     */

    public int code;
    public List<MessageBean> message;


    public static class MessageBean {
        /**
         * payAmount : 1200
         * amount : 60
         * userPayNO : 15029971182
         * createDate : 1482722616000
         * balance : 0
         */

        public int payAmount;
        public int amount;
        public String userPayNO;
        public long createDate;
        public int balance;

    }
}
