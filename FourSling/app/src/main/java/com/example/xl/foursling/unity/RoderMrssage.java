package com.example.xl.foursling.unity;

import java.util.List;

/**
 * Created by admin on 2017/1/10.
 */
public class RoderMrssage {


    /**
     * code : 0
     * message : {"rows":[{"parcelNO":"J02901000000002","createDate":"2017-01-12 11:50:48.0","title":"接单消息","name1":"西安分拨中心","name2":"洛川分拨中心","name3":"西安分拨中心","name4":"洛川分拨中心"},{"parcelNO":"J02901000000002","createDate":"2017-01-12 11:51:28.0","title":"订单取消","name1":"西安分拨中心","name2":"洛川分拨中心","name3":"西安分拨中心","name4":"洛川分拨中心"}]}
     */

    public int code;
    public MessageBean message;

    public static class MessageBean {
        public List<RowsBean> rows;

        public static class RowsBean {
            /**
             * parcelNO : J02901000000002
             * createDate : 2017-01-12 11:50:48.0
             * title : 接单消息
             * name1 : 西安分拨中心
             * name2 : 洛川分拨中心
             * name3 : 西安分拨中心
             * name4 : 洛川分拨中心
             */

            public String parcelNO;
            public String createDate;
            public String title;
            public String name1;
            public String name2;
            public String name3;
            public String name4;

        }
    }
}
