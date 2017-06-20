package com.example.xl.foursling.activityes.map;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.LatLonPoint;
import com.example.xl.foursling.R;

public class GPSNaviActivity extends BaseMapActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!TextUtils.isEmpty(getIntent().getStringExtra("start_x"))&&!TextUtils.isEmpty(getIntent().getStringExtra("start_y"))
                &&!TextUtils.isEmpty(getIntent().getStringExtra("end_x"))&&!TextUtils.isEmpty(getIntent().getStringExtra("end_y"))) {
            mStartLatlng = new NaviLatLng(Double.parseDouble(getIntent().getStringExtra("start_x")),
                    Double.parseDouble(getIntent().getStringExtra("start_y")));//起点，116.335891,39.942295
            mEndLatlng = new NaviLatLng(Double.parseDouble(getIntent().getStringExtra("end_x")),
                    Double.parseDouble(getIntent().getStringExtra("end_y")));
        }else {
            Toast.makeText(getApplication(),"无法获取当前路径规划",Toast.LENGTH_LONG).show();
        }
//        mStartLatlng = new NaviLatLng(34.209389, 109.013799);
//        mEndLatlng = new NaviLatLng(45.742367, 126.661665);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_basic_navi);
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        /**
         * 方法: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); 参数:
         *
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         *  说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         *  注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
         */
        int strategy = 0;
        try {
            //再次强调，最后一个参数为true时代表多路径，否则代表单路径
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);

    }

    @Override
    public void onCalculateRouteSuccess() {
        super.onCalculateRouteSuccess();
        mAMapNavi.startNavi(NaviType.GPS);
    }
}
