package com.example.xl.foursling.unity;

import java.util.List;

/**
 * Created by admin on 2017/1/9.
 */

public class RouteContent {

    /**
     * code : 0
     * message : {"rows":[{"parcelNO":"J02901000000003","company_companyLon":"西安分拨中心","company_companyLat":"洛川分拨中心","startCompanyLon":"45.742367","startCompanyLat":"126.661665","endCompanyLon":"34.209389","endCompanyLat":"109.013799","startUserPhone":"15871725059","startAddress":"kjkljlk","endUserPhone":"15721986588","endAddress":"ssf","weight":10000,"distance":1000,"moments":138,"status":1,"createDate":"2017-01-07 11:14:29","parcelStatus":0,"yunCost":1000},{"parcelNO":"J02901000000001","company_companyLon":"西安分拨中心","company_companyLat":"洛川分拨中心","startCompanyLon":"45.742367","startCompanyLat":"126.661665","endCompanyLon":"34.209389","endCompanyLat":"109.013799","startUserPhone":"15871725059","startAddress":"kjkljlk","endUserPhone":"15721986588","endAddress":"ssf","weight":15000,"distance":1000000,"moments":0,"status":1,"createDate":"2017-01-04 09:36:35","parcelStatus":0,"yunCost":2100},{"parcelNO":"J02901000000002","company_companyLon":"西安分拨中心","company_companyLat":"洛川分拨中心","startCompanyLon":"45.742367","startCompanyLat":"126.661665","endCompanyLon":"34.209389","endCompanyLat":"109.013799","startUserPhone":"15871725059","startAddress":"kjkljlk","endUserPhone":"15721986588","endAddress":"ssf","weight":10000,"distance":1000,"moments":138,"status":1,"createDate":"2017-01-07 11:14:29","parcelStatus":0,"yunCost":1000}]}
     */

    public int code;
    public MessageBean message;
    public static class MessageBean {
        public List<RowsBean> rows;
        public static class RowsBean {
            /**
             * parcelNO : J02901000000003
             * company_companyLon : 西安分拨中心
             * company_companyLat : 洛川分拨中心
             * startCompanyLon : 45.742367
             * startCompanyLat : 126.661665
             * endCompanyLon : 34.209389
             * endCompanyLat : 109.013799
             * startUserPhone : 15871725059
             * startAddress : kjkljlk
             * endUserPhone : 15721986588
             * endAddress : ssf
             * weight : 10000
             * distance : 1000
             * moments : 138
             * status : 1
             * createDate : 2017-01-07 11:14:29
             * parcelStatus : 0
             * yunCost : 1000
             */

            public String parcelNO;
            public String company_companyLon;
            public String company_companyLat;
            public String startCompanyLon;
            public String startCompanyLat;
            public String endCompanyLon;
            public String endCompanyLat;
            public String startUserPhone;
            public String startAddress;
            public String endUserPhone;
            public String endAddress;
            public int weight;
            public int distance;
            public long moments;
            public int status;
            public String createDate;
            public int parcelStatus;
            public int yunCost;

        }
    }
}
