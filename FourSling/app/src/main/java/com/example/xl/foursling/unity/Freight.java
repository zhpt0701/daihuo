package com.example.xl.foursling.unity;

import java.util.List;

/**
 * Created by admin on 2017/1/5.
 */

public class Freight {


    /**
     * code : 0
     * message : [{"serviceCode":"766b8fb144151e927bbd91a48fd4d505","messageTitle":"结算消息","messageRemark":"结算消息","messageContent":"您于:2017-01-10 19:04:26成功收到运费:10.0元整,货物编号为:J02901000000002,原运费为10.0元,由于您提前送达货物,按照规定奖励您0.0元.","messageUrl":null,"messagePic":null,"message_type_messageCode":"XL004","status":0,"createDate":1484047564000,"userCode":"15091398240","readCode":1},{"serviceCode":"b861124a496aa8952fa44d23f00928bd","messageTitle":"结算消息","messageRemark":"结算消息","messageContent":"您于:2017-01-10 19:17:01成功收到运费:10.0元整,货物编号为:J02901000000003,原运费为10.0元,由于您提前送达货物,按照规定奖励您0.0元.","messageUrl":null,"messagePic":null,"message_type_messageCode":"XL004","status":0,"createDate":1484046078000,"userCode":"15091398240","readCode":1}]
     */

    public int code;
    public List<MessageBean> message;
    public static class MessageBean {
        /**
         * serviceCode : 766b8fb144151e927bbd91a48fd4d505
         * messageTitle : 结算消息
         * messageRemark : 结算消息
         * messageContent : 您于:2017-01-10 19:04:26成功收到运费:10.0元整,货物编号为:J02901000000002,原运费为10.0元,由于您提前送达货物,按照规定奖励您0.0元.
         * messageUrl : null
         * messagePic : null
         * message_type_messageCode : XL004
         * status : 0
         * createDate : 1484047564000
         * userCode : 15091398240
         * readCode : 1
         */

        public String serviceCode;
        public String messageTitle;
        public String messageRemark;
        public String messageContent;
        public Object messageUrl;
        public Object messagePic;
        public String message_type_messageCode;
        public int status;
        public long createDate;
        public String userCode;
        public int readCode;

    }
}
