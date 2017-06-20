package com.example.xl.foursling.unity;

import java.util.List;

/**
 * Created by admin on 2017/1/14.
 */

public class UserData {

    /**
     * code : 1
     * message : [{"serviceCode":"1467e7133bba11af9205875f21de1a70","idhUser_serviceCode":null,"userName":"王小欢","idCard":"610622198904210328","idCardPic":"http://pic.mastershao.cn/idaihuo3.0/12345/1483098655715.png","handheldCardPic":"http://pic.mastershao.cn/idaihuo3.0/12345/1483098656371.png","bust":"http://pic.mastershao.cn/idaihuo3.0/15029971182/1483098653675.png","createDate":1482375241000,"modificationTime":null,"level":null,"wechatNO":"4649499","address":null,"alipayNO":"15091398240","userPhone":"15091398240","status":0,"drivalPic":"http://pic.mastershao.cn/idaihuo3.0/12345/1483098657034.png","userCode":"15091398240","carName":"嘻嘻嘻嘻","carCode":"西A12345","carPic":"http://pic.mastershao.cn/idaihuo3.0/123456/1482375280955.png","tracelPic":"http://pic.mastershao.cn/idaihuo3.0/123456/1482375291350.png","car_type_code":"6","usingStatus":0,"insurancePic":"http://pic.mastershao.cn/idaihuo3.0/123456/1482375296025.png"}]
     */

    public int code;
    public List<MessageBean> message;

    public static class MessageBean {
        /**
         * serviceCode : 1467e7133bba11af9205875f21de1a70
         * idhUser_serviceCode : null
         * userName : 王小欢
         * idCard : 610622198904210328
         * idCardPic : http://pic.mastershao.cn/idaihuo3.0/12345/1483098655715.png
         * handheldCardPic : http://pic.mastershao.cn/idaihuo3.0/12345/1483098656371.png
         * bust : http://pic.mastershao.cn/idaihuo3.0/15029971182/1483098653675.png
         * createDate : 1482375241000
         * modificationTime : null
         * level : null
         * wechatNO : 4649499
         * address : null
         * alipayNO : 15091398240
         * userPhone : 15091398240
         * status : 0
         * drivalPic : http://pic.mastershao.cn/idaihuo3.0/12345/1483098657034.png
         * userCode : 15091398240
         * carName : 嘻嘻嘻嘻
         * carCode : 西A12345
         * carPic : http://pic.mastershao.cn/idaihuo3.0/123456/1482375280955.png
         * tracelPic : http://pic.mastershao.cn/idaihuo3.0/123456/1482375291350.png
         * car_type_code : 6
         * usingStatus : 0
         * insurancePic : http://pic.mastershao.cn/idaihuo3.0/123456/1482375296025.png
         */
        public String carTypeName;
        public int carWeight;
        public String serviceCode;
        public Object idhUser_serviceCode;
        public String userName;
        public String idCard;
        public String idCardPic;
        public String handheldCardPic;
        public String bust;
        public long createDate;
        public Object modificationTime;
        public Object level;
        public String wechatNO;
        public Object address;
        public String alipayNO;
        public String userPhone;
        public int status;
        public String drivalPic;
        public String userCode;
        public String carName;
        public String carCode;
        public String carPic;
        public String tracelPic;
        public String car_type_code;
        public int usingStatus;
        public String insurancePic;
    }
}
