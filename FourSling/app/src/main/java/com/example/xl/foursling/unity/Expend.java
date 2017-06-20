package com.example.xl.foursling.unity;

import java.util.List;

/**
 * Created by admin on 2016/12/26.
 */

public class Expend {

    /**
     * code : 0
     * message : [{"payAmount":8000,"amount":60,"payType":1,"createDate":1482741423000,"balance":890800},{"payAmount":2500,"amount":60,"payType":1,"createDate":1482734032000,"balance":949160},{"payAmount":50000,"amount":60,"payType":1,"createDate":1482734599000,"balance":899100},{"payAmount":0,"amount":60,"payType":1,"createDate":1482748122000,"balance":941040},{"payAmount":8000,"amount":60,"payType":1,"createDate":1482741423000,"balance":890800},{"payAmount":2500,"amount":60,"payType":1,"createDate":1482734032000,"balance":949160},{"payAmount":50000,"amount":60,"payType":1,"createDate":1482734599000,"balance":899100},{"payAmount":0,"amount":60,"payType":1,"createDate":1482748122000,"balance":941040},{"payAmount":8000,"amount":60,"payType":1,"createDate":1482741423000,"balance":890800},{"payAmount":2500,"amount":60,"payType":1,"createDate":1482734032000,"balance":949160},{"payAmount":50000,"amount":60,"payType":1,"createDate":1482734599000,"balance":899100},{"payAmount":0,"amount":60,"payType":1,"createDate":1482748122000,"balance":941040},{"payAmount":8000,"amount":300,"payType":1,"createDate":1482741423000,"balance":890800},{"payAmount":2500,"amount":300,"payType":1,"createDate":1482734032000,"balance":949160},{"payAmount":50000,"amount":300,"payType":1,"createDate":1482734599000,"balance":899100},{"payAmount":0,"amount":300,"payType":1,"createDate":1482748122000,"balance":941040},{"payAmount":8000,"amount":300,"payType":1,"createDate":1482741423000,"balance":890800},{"payAmount":2500,"amount":300,"payType":1,"createDate":1482734032000,"balance":949160},{"payAmount":50000,"amount":300,"payType":1,"createDate":1482734599000,"balance":899100},{"payAmount":0,"amount":300,"payType":1,"createDate":1482748122000,"balance":941040}]
     */

    public int code;
    public List<MessageBean> message;



    public static class MessageBean {
        /**
         * payAmount : 8000
         * amount : 60
         * payType : 1
         * createDate : 1482741423000
         * balance : 890800
         */

        public int payAmount;
        public int amount;
        public int payType;
        public long createDate;
        public int balance;

    }
}
