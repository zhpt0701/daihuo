package com.example.xl.foursling.unity;

import java.util.List;

/**
 * Created by admin on 2016/12/26.
 */

public class Messagees {

    /**
     * code : 0
     * message : [{"phone":"18829017823","messageTitle":"11","createDate":1482750798000,"messageUrl":"http://i.firefoxchina.cn/?www.firefoxchina.cn"},{"phone":"18829017823","messageTitle":"12","createDate":1482751076000,"messageUrl":"http://www.sina.com.cn/"}]
     */

    public int code;
    public List<MessageBean> message;


    public static class MessageBean {
        /**
         * phone : 18829017823
         * messageTitle : 11
         * createDate : 1482750798000
         * messageUrl : http://i.firefoxchina.cn/?www.firefoxchina.cn
         */

        public String phone;
        public String messageTitle;
        public long createDate;
        public String messageUrl;
        public int readCode;

    }
}
