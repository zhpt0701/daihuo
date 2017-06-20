package com.example.xl.foursling.unity;

import java.util.List;

/**
 * Created by admin on 2017/2/13.
 */

public class SeachRoderUnity {

    /**
     * code : 0
     * message : [{"rows":[{"parcelNO":"J02901000007908","company_companyLon":"西安分拨中心","company_companyLat":"延安分拨中心","startCompanyLon":"108.872118","startCompanyLat":"34.254816","endCompanyLon":"109.467468","endCompanyLat":"36.583181","startUserPhone":"13759961494","startAddress":"","endAddress":"","weight":18000,"distance":312028,"moments":16500,"createDate":"2017-02-13 11:03:27","parcelStatus":1,"yunCost":2000}]}]
     */

    public int code;
    public List<MessageBean> message;

    public static class MessageBean {
        public List<RowsBean> rows;

        public static class RowsBean {
            /**
             * parcelNO : J02901000007908
             * company_companyLon : 西安分拨中心
             * company_companyLat : 延安分拨中心
             * startCompanyLon : 108.872118
             * startCompanyLat : 34.254816
             * endCompanyLon : 109.467468
             * endCompanyLat : 36.583181
             * startUserPhone : 13759961494
             * startAddress :
             * endAddress :
             * weight : 18000
             * distance : 312028
             * moments : 16500
             * createDate : 2017-02-13 11:03:27
             * parcelStatus : 1
             * yunCost : 2000
             */
            public String parcelNO;
            public String company_companyLon;
            public String company_companyLat;
            public String startCompanyLon;
            public String startCompanyLat;
            public String endCompanyLon;
            public String endCompanyLat;
            public String startUserPhone;
            public String endUserPhone;
            public String startAddress;
            public String endAddress;
            public int weight;
            public int distance;
            public long moments;
            public String createDate;
            public int parcelStatus;
            public int yunCost;
        }
    }
}
