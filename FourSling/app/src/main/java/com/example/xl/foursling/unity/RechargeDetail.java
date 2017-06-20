package com.example.xl.foursling.unity;

import java.util.List;

/**
 * Created by admin on 2016/12/26.
 * 充值明细
 */

public class RechargeDetail {

    /**
     * code : 0
     * message : [{"payAmount":11,"createDate":1482733364000,"type_service":null,"orderCode":"161226"}]
     */

    public int code;
    public List<MessageBean> message;


    public static class MessageBean {
        /**
         * payAmount : 11
         * createDate : 1482733364000
         * type_service : null
         * orderCode : 161226
         */

        public int payAmount;
        public long createDate;
        public Object type_service;
        public String orderCode;

    }
}
