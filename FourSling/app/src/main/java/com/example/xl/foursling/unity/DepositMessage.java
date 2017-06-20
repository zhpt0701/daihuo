package com.example.xl.foursling.unity;

import java.util.List;

/**
 * Created by admin on 2016/12/27.
 */

public class DepositMessage {

    /**
     * code : 0
     * message : [{"tradingOrderNO":"161226143352366942","tradingAmount":2500,"createDate":1482734032000,"status":1,"payType":1,"userPayNO":"18829017823"},{"tradingOrderNO":"161226143352366942","tradingAmount":2500,"createDate":1482734032000,"status":1,"payType":1,"userPayNO":"18829017823"},{"tradingOrderNO":"161226144319629199","tradingAmount":50000,"createDate":1482734599000,"status":2,"payType":1,"userPayNO":"18829017823"},{"tradingOrderNO":"161226144319629199","tradingAmount":50000,"createDate":1482734599000,"status":2,"payType":1,"userPayNO":"18829017823"},{"tradingOrderNO":"161226144319629199","tradingAmount":50000,"createDate":1482734599000,"status":2,"payType":1,"userPayNO":"18829017823"},{"tradingOrderNO":"161226144319629199","tradingAmount":50000,"createDate":1482734599000,"status":2,"payType":1,"userPayNO":"18829017823"},{"tradingOrderNO":"161226","tradingAmount":11,"createDate":1482733469000,"status":1,"payType":1,"userPayNO":"喜来"},{"tradingOrderNO":"161226182842785382","tradingAmount":0,"createDate":1482748122000,"status":1,"payType":1,"userPayNO":"18829017823"},{"tradingOrderNO":"161226182842785382","tradingAmount":0,"createDate":1482748122000,"status":1,"payType":1,"userPayNO":"18829017823"},{"tradingOrderNO":"161226163703670212","tradingAmount":8000,"createDate":1482741423000,"status":0,"payType":1,"userPayNO":"18829017823"},{"tradingOrderNO":"161226163703670212","tradingAmount":8000,"createDate":1482741423000,"status":0,"payType":1,"userPayNO":"18829017823"}]
     */

    public int code;
    public List<MessageBean> message;
    public static class MessageBean {
        /**
         * tradingOrderNO : 161226143352366942
         * tradingAmount : 2500
         * createDate : 1482734032000
         * status : 1
         * payType : 1
         * userPayNO : 18829017823
         */

        public String tradingOrderNO;
        public int tradingAmount;
        public long createDate;
        public int status;
        public int payType;
        public String userPayNO;

    }
}
