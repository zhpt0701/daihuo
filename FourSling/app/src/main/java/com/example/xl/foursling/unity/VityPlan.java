package com.example.xl.foursling.unity;

import java.util.List;

/**
 * Created by admin on 2016/12/27.
 */

public class VityPlan {

    /**
     * code : 0
     * message : [{"status":1,"tradeDate":null}]
     */

    public int code;
    public List<MessageBean> message;


    public static class MessageBean {
        /**
         * status : 1
         * tradeDate : null
         */

        public int status;
        public long tradeDate;
        public long createDate;

    }
}
