package com.example.xl.foursling.unity;

import com.example.xl.foursling.tools.GsonUtil;

import java.util.List;

/**
 * Created by admin on 2016/12/21.
 */
public class AddCar{

    /**
     * code : 0
     * message : [{"userCar":null,"carCode":"山X12341","carType":"1","carPic":"http://pic.mastershao.cn/idaihuo3.0/123456/1482287166149.png","weight":null,"carName":"一句句","usingStatus":1,"status":1},{"userCar":null,"carCode":"山a12345","carType":"1","carPic":"http://pic.mastershao.cn/idaihuo3.0/123456/1482290925415.png","weight":null,"carName":"123","usingStatus":1,"status":1},{"userCar":null,"carCode":"山A12345","carType":"1","carPic":"http://pic.mastershao.cn/idaihuo3.0/123456/1482290925415.png","weight":null,"carName":"123","usingStatus":1,"status":1},{"userCar":null,"carCode":"山X12345","carType":"1","carPic":"http://pic.mastershao.cn/idaihuo3.0/123456/1482287166149.png","weight":null,"carName":"一句句","usingStatus":1,"status":1},{"userCar":null,"carCode":"钱W12345","carType":"1","carPic":"http://pic.mastershao.cn/idaihuo3.0/123456/1482288912652.png","weight":null,"carName":"日理万机","usingStatus":0,"status":1},{"userCar":null,"carCode":"钱W12345","carType":"1","carPic":"http://pic.mastershao.cn/idaihuo3.0/123456/1482288912652.png","weight":null,"carName":"日理万机","usingStatus":0,"status":1},{"userCar":null,"carCode":"山A12345","carType":"1","carPic":"http://pic.mastershao.cn/idaihuo3.0/123456/1482290925415.png","weight":null,"carName":"123","usingStatus":1,"status":1},{"userCar":null,"carCode":"山B12345","carType":"1","carPic":"http://pic.mastershao.cn/idaihuo3.0/123456/1482285121328.png","weight":null,"carName":"大众","usingStatus":1,"status":1},{"userCar":null,"carCode":"山a12345","carType":"1","carPic":"http://pic.mastershao.cn/idaihuo3.0/123456/1482290925415.png","weight":null,"carName":"123","usingStatus":1,"status":1}]
     */

    public  int code;
    public  List<MessageBean> message;


    public static class MessageBean {
        /**
         * userCar : null
         * carCode : 山X12341
         * carType : 1
         * carPic : http://pic.mastershao.cn/idaihuo3.0/123456/1482287166149.png
         * weight : null
         * carName : 一句句
         * usingStatus : 1
         * status : 1
         */

        public  Object userCar;
        public  String carCode;
        public  String carTypeCode;
        public  String carPic;
        public  Object weight;
        public  String carName;
        public  int carWeight;
        public  int usingStatus;
        public  int status;


    }
}
