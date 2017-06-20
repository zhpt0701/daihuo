package com.example.xl.foursling.unity;

/**
 * Created by admin on 2016/12/23.
 */

public class User {
    /**
     * code : 0
     * message : {"userSex":1,"userPic":"http://pic.mastershao.cn/idaihuo3.0/123456/1482390978845.png","userName":"Whhh","wechatNo":"Snsjsjn","address":"贵州省安顺市关岭布依族苗族自治县","alipayNo":"15029971182","status":1,"carCode":"钱W12345","phone":"15029971182"}
     */
    public int code;
    public MessageBean message;
    public static class MessageBean {
        /**
         * userSex : 1
         * userPic : http://pic.mastershao.cn/idaihuo3.0/123456/1482390978845.png
         * userName : Whhh
         * wechatNo : Snsjsjn
         * address : 贵州省安顺市关岭布依族苗族自治县
         * alipayNo : 15029971182
         * status : 1
         * carCode : 钱W12345
         * phone : 15029971182
         */
        public int userSex;
        public String userPic;
        public String userName;
        public String wechatNo;
        public String address;
        public String alipayNo;
        public int status;
        public String carCode;
        public String phone;
    }
}
