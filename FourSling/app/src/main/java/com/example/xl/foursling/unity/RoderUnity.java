package com.example.xl.foursling.unity;

import java.util.List;

/**
 * Created by admin on 2017/1/4.
 */

public class RoderUnity {
    /**
     * code : 0
     * message : {"rows":[{"parcelNO":"J02901000007904","company_companyLon":"西安分拨中心","company_companyLat":"西安分拨中心","startCompanyLon":"108.872118","startCompanyLat":"34.254816","endCompanyLon":"108.872118","endCompanyLat":"34.254816","startUserPhone":"13759961494","startAddress":"","endUserPhone":"13759961494","endAddress":"","weight":43000,"distance":1000,"moments":0,"createDate":"2017-02-13 18:04:39","parcelStatus":0,"yunCost":2000},{"parcelNO":"J02901000007909","company_companyLon":"","company_companyLat":"","startCompanyLon":"","startCompanyLat":"","endCompanyLon":"","endCompanyLat":"","userPhone1":"","address1":"","userPhone2":"","address2":"","weight":3000,"distance":1000,"moments":0,"createDate":"2017-02-13 11:14:26","parcelStatus":0,"yunCost":1000}]}
     */
    public int code;
    public MessageBean message;

    public static class MessageBean {
        public List<RowsBean> rows;

        public static class RowsBean {
            /**
             * parcelNO : J02901000007904
             * company_companyLon : 西安分拨中心
             * company_companyLat : 西安分拨中心
             * startCompanyLon : 108.872118
             * startCompanyLat : 34.254816
             * endCompanyLon : 108.872118
             * endCompanyLat : 34.254816
             * startUserPhone : 13759961494
             * startAddress :
             * endUserPhone : 13759961494
             * endAddress :
             * weight : 43000
             * distance : 1000
             * moments : 0
             * createDate : 2017-02-13 18:04:39
             * parcelStatus : 0
             * yunCost : 2000
             * userPhone1 :
             * address1 :
             * userPhone2 :
             * address2 :
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
            public int moments;
            public String createDate;
            public int parcelStatus;
            public int yunCost;
            public String userPhone1;
            public String address1;
            public String userPhone2;
            public String address2;
        }
    }
}
