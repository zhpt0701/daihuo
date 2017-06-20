package com.example.xl.foursling.activityes.map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.WalkRouteResult;
import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.tools.ToastUtil;
import com.example.xl.foursling.tools.navi.AMapUtil;
import com.example.xl.foursling.tools.navi.DrivingRouteOverLay;
import com.example.xl.foursling.tools.navi.RouteOverlay;
import com.example.xl.foursling.view.titlebar.TeaskBar;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class DriveRouteActivity extends BaseActivity implements OnMapClickListener,
		OnMarkerClickListener, OnInfoWindowClickListener, InfoWindowAdapter, OnRouteSearchListener, OnClickListener {
	@Bind(R.id.route_activity_title)
	LinearLayout route_activity_title;
	@Bind(R.id.route_activity_back)
	Button route_activity_back;
	@Bind(R.id.route_activity_map)
	Button route_activity_map;
	@Bind(R.id.route_activity_popup)
	ImageButton route_activity_popup;
	private AMap aMap;
	private MapView mapView;
	private Context mContext;
	private RouteSearch mRouteSearch;
	private DriveRouteResult mDriveRouteResult;
	private LatLonPoint mStartPoint;
	private LatLonPoint mEndPoint;
	private final int ROUTE_TYPE_DRIVE = 2;
	private PopupWindow popupWindow;
	private View view;
	private ProgressDialog progDialog = null;// 搜索时进度条
	private ImageButton popup_roder_dateil_imagebtn_one;
	private ImageButton popup_roder_dateil_imagebtn_two;
	private ImageView popup_roder_dateil_image_one;
	private ImageView popup_roder_dateil_image_two;
	private ImageView popup_roder_dateil_image_three;
	private TextView popup_roder_dateil_text_two;
	private TextView popup_roder_dateil_text_one;
	private TextView popup_roder_dateil_text_time;
	private ImageButton popup_roder_dateil_imagebtn_over;
	private TextView popup_roder_dateil_text_yf;
	private TextView popup_roder_dateil_text_dd;
	private TextView popup_roder_dateil_text_end_address;
	private TextView popup_roder_dateil_text_address;
	private TextView popup_roder_dateil_text_end;
	private TextView popup_roder_dateil_text_start;
	private TextView popup_roder_dateil_text_long;
	private TextView popup_roder_dateil_start;
	private String des;
	private String phone;
	private LatLonPoint wayPoints;
	private List<LatLonPoint> list;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.route_activity);
		TeaskBar.onSystemoutcolor(this, Constants.ZERO);
		ButterKnife.bind(this);
		mContext = this.getApplicationContext();
		mapView = (MapView) findViewById(R.id.route_map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		initData();
		init();
		setfromandtoMarker();
		searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
	}

	private void initData() {
//		mStartPoint = new LatLonPoint(34.209389,109.013799);
//		mEndPoint = new LatLonPoint(45.742367,126.661665);
		if (!TextUtils.isEmpty(getIntent().getStringExtra("start_x"))&&!TextUtils.isEmpty(getIntent().getStringExtra("start_y"))
				&&!TextUtils.isEmpty(getIntent().getStringExtra("end_x"))&&!TextUtils.isEmpty(getIntent().getStringExtra("end_y"))) {
//			mStartPoint = new LatLonPoint(Double.parseDouble(SharePreferencesUlits.getString(getApplication(),CharConstants.LATITUDE,"")),
//					Double.parseDouble(SharePreferencesUlits.getString(getApplication(),CharConstants.LONGITUDE,"")));//起点，116.335891,39.942295
//			wayPoints = new LatLonPoint(39.925041,116.437901);
//			mEndPoint = new LatLonPoint(45.742367,126.661665);
			mStartPoint = new LatLonPoint(Double.parseDouble(getIntent().getStringExtra("start_x")),
					Double.parseDouble(getIntent().getStringExtra("start_y")));//起点，116.335891,39.942295
//			wayPoints = new LatLonPoint(Double.parseDouble(getIntent().getStringExtra("start_x")),
//					Double.parseDouble(getIntent().getStringExtra("start_y")));
			mEndPoint = new LatLonPoint(Double.parseDouble(getIntent().getStringExtra("end_x")),
					Double.parseDouble(getIntent().getStringExtra("end_y")));
		}else {
			Toast.makeText(getApplication(),"无法获取当前路径规划",Toast.LENGTH_LONG).show();
		}
	}

	private void setfromandtoMarker() {
		aMap.addMarker(new MarkerOptions()
		.position(AMapUtil.convertToLatLng(mStartPoint))
		.icon(BitmapDescriptorFactory.fromResource(R.mipmap.search_icon_start)));

		aMap.addMarker(new MarkerOptions()
		.position(AMapUtil.convertToLatLng(mEndPoint))
		.icon(BitmapDescriptorFactory.fromResource(R.mipmap.search_icon_end)));
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();	
		}
		registerListener();
		mRouteSearch = new RouteSearch(this);
		mRouteSearch.setRouteSearchListener(this);
		route_activity_back.setOnClickListener(this);
		route_activity_popup.setOnClickListener(this);
		route_activity_map.setOnClickListener(this);
	}

	/**
	 * 注册监听
	 */
	private void registerListener() {
		aMap.setOnMapClickListener(DriveRouteActivity.this);
		aMap.setOnMarkerClickListener(DriveRouteActivity.this);
		aMap.setOnInfoWindowClickListener(DriveRouteActivity.this);
		aMap.setInfoWindowAdapter(DriveRouteActivity.this);
	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 开始搜索路径规划方案
	 */
	public void searchRouteResult(int routeType, int mode) {
		if (mStartPoint == null) {
			ToastUtil.show(mContext, "定位中，稍后再试...");
			return;
		}
		if (mEndPoint == null) {
			ToastUtil.show(mContext, "终点未设置");
		}
		showProgressDialog();
		final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
				mStartPoint, mEndPoint);
//		list = new ArrayList<>();
//		list.add(wayPoints);
		if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
			DriveRouteQuery query = new DriveRouteQuery(fromAndTo, mode, null,
					null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
			mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
		}
	}

	@Override
	public void onBusRouteSearched(BusRouteResult result, int errorCode) {
		
	}

	@Override
	public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
		dissmissProgressDialog();
		aMap.clear();// 清理地图上的所有覆盖物
		if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
			if (result != null && result.getPaths() != null) {
				if (result.getPaths().size() > 0) {
					mDriveRouteResult = result;
					final DrivePath drivePath = mDriveRouteResult.getPaths()
							.get(0);
					DrivingRouteOverLay drivingRouteOverlay = new DrivingRouteOverLay(
							mContext, aMap, drivePath,
							mDriveRouteResult.getStartPos(),
							mDriveRouteResult.getTargetPos(), null);
					drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
					drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
					drivingRouteOverlay.removeFromMap();
					drivingRouteOverlay.addToMap();
					drivingRouteOverlay.zoomToSpan();
					int dis = (int) drivePath.getDistance();
					int dur = (int) drivePath.getDuration();
					//时间
					des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
//					mRotueTimeDes.setText(des);
//					mRouteDetailDes.setVisibility(View.VISIBLE);
//					int taxiCost = (int) mDriveRouteResult.getTaxiCost();
//					mRouteDetailDes.setText("打车约"+taxiCost+"元");
				} else if (result != null && result.getPaths() == null) {
					ToastUtil.show(mContext, R.string.no_result);
				}

			} else {
				ToastUtil.show(mContext, R.string.no_result);
			}
		} else {
			ToastUtil.showerror(this.getApplicationContext(), errorCode);
		}
		
		
	}

	@Override
	public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
		
	}

	/**
	 * 显示进度框
	 */
	private void showProgressDialog() {
		if (progDialog == null)
			progDialog = new ProgressDialog(this);
		    progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		    progDialog.setIndeterminate(false);
		    progDialog.setCancelable(true);
		    progDialog.setMessage("正在搜索");
		    progDialog.show();
	    }

	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onRideRouteSearched(RideRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClick(View view) {
		Intent intent = new Intent();
		switch (view.getId()){
			case R.id.route_activity_back:
				finish();
				break;
			case R.id.route_activity_map:

				intent.setClass(getApplication(),GPSNaviActivity.class);
				intent.putExtra("start_x",getIntent().getStringExtra("start_x"));
				intent.putExtra("start_y",getIntent().getStringExtra("start_y"));
				intent.putExtra("end_x",getIntent().getStringExtra("end_x"));
				intent.putExtra("end_y",getIntent().getStringExtra("end_y"));
				startActivity(intent);
				break;
			case R.id.route_activity_popup:
				route_activity_popup.setVisibility(View.INVISIBLE);
				initpopup(true);
				break;
			case R.id.popup_roder_dateil_imagebtn_over:
				route_activity_popup.setVisibility(View.VISIBLE);
				popupWindow.dismiss();
				break;
			case R.id.popup_roder_dateil_imagebtn_one:
				initPerron_phone(getIntent().getStringExtra("start_phone"));
				break;
			case R.id.popup_roder_dateil_imagebtn_two:
				initPerron_phone(getIntent().getStringExtra("end_phone"));
				break;
		}
	}
	private void initPerron_phone(String phone_number) {
		phone = phone_number;
		Intent intent = new Intent();
		//android 6.0 版本权限获取判断
		Log.d("this_content_sdk", Build.VERSION.SDK_INT+"");
		if (Build.VERSION.SDK_INT >= 23) {
			int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, "android.permission.CALL_PHONE");
			Log.d("this_content_sdk",checkCallPhonePermission+"");
			if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this, new String[]{"android.permission.CALL_PHONE"}, 2);
			}else {
				intent.setAction(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:" + phone_number));
				startActivity(intent);
			}
		}else {
			intent.setAction(Intent.ACTION_CALL);
			intent.setData(Uri.parse("tel:" + phone_number));
			startActivity(intent);
		}
	}
	/**
	 * android 6.0 版本权限获取回掉事件
	 * @param requestCode
	 * @param permissions
	 * @param grantResults
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_CALL);
			intent.setData(Uri.parse("tel:"+phone));
			startActivity(intent);
		} else {
			Toast.makeText(getApplication(),"未给予权限，部分功能无法正常使用",Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * 接单详情数据
	 * @param b
     */
	private void initpopup(boolean b) {
		popupWindow = new PopupWindow();
		view = LayoutInflater.from(this).inflate(R.layout.popup_roder_dateil,null);
		popupWindow.setContentView(view);
		//定义popupwindows宽高
		popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setClippingEnabled(true);
		popup_roder_dateil_image_one = (ImageView)view.findViewById(R.id.popup_roder_dateil_image_one);
		popup_roder_dateil_text_one = (TextView)view.findViewById(R.id.popup_roder_dateil_text_one);

		popup_roder_dateil_image_two = (ImageView)view.findViewById(R.id.popup_roder_dateil_image_two);
		popup_roder_dateil_text_two = (TextView)view.findViewById(R.id.popup_roder_dateil_text_two);

		popup_roder_dateil_image_three = (ImageView)view.findViewById(R.id.popup_roder_dateil_image_three);
		//当前位置
		popup_roder_dateil_start = (TextView)view.findViewById(R.id.popup_roder_dateil_start);
		//到当前分拨中心距离
		popup_roder_dateil_text_long = (TextView)view.findViewById(R.id.popup_roder_dateil_text_long);
		//出发位置
		popup_roder_dateil_text_start = (TextView)view.findViewById(R.id.popup_roder_dateil_text_start);
		popup_roder_dateil_text_address = (TextView)view.findViewById(R.id.popup_roder_dateil_text_address);
		//结束位置
		popup_roder_dateil_text_end = (TextView)view.findViewById(R.id.popup_roder_dateil_text_end);
		popup_roder_dateil_text_end_address = (TextView)view.findViewById(R.id.popup_roder_dateil_text_end_address);
		//订单号
		popup_roder_dateil_text_dd = (TextView)view.findViewById(R.id.popup_roder_dateil_text_dd);
		//运费
		popup_roder_dateil_text_yf = (TextView)view.findViewById(R.id.popup_roder_dateil_text_yf);
		//时间
		popup_roder_dateil_text_time = (TextView)view.findViewById(R.id.popup_roder_dateil_text_time);
		//关闭opup
		popup_roder_dateil_imagebtn_over = (ImageButton)view.findViewById(R.id.popup_roder_dateil_imagebtn_over);
		//phone_one
		popup_roder_dateil_imagebtn_one = (ImageButton)view.findViewById(R.id.popup_roder_dateil_imagebtn_one);
		//phone_two
		popup_roder_dateil_imagebtn_two = (ImageButton)view.findViewById(R.id.popup_roder_dateil_imagebtn_two);
		popup_roder_dateil_imagebtn_over.setOnClickListener(this);
		popup_roder_dateil_imagebtn_one.setOnClickListener(this);
		popup_roder_dateil_imagebtn_two.setOnClickListener(this);

		popup_roder_dateil_text_long.setText(String.valueOf(StringUilts.doubleMoney(StringUilts.calculateDistances(
				Double.parseDouble(SharePreferencesUlits.getString(getApplication(),CharConstants.LATITUDE,"")),
				Double.parseDouble(SharePreferencesUlits.getString(getApplication(),CharConstants.LONGITUDE,"")),
				Double.parseDouble(getIntent().getStringExtra("start_x")),
				Double.parseDouble(getIntent().getStringExtra("start_y")))/1000))+"公里");
		popup_roder_dateil_start.setText("我的位置("+SharePreferencesUlits.getString(getApplication(),CharConstants.THIS_ADDRESS,"")+")");
		if (!TextUtils.isEmpty(getIntent().getStringExtra("start"))){
			popup_roder_dateil_text_start.setText("始发地("+getIntent().getStringExtra("start")+")");
		}
		if (!TextUtils.isEmpty(getIntent().getStringExtra("start_address"))){
			popup_roder_dateil_text_address.setText(getIntent().getStringExtra("start_address"));
		}
		if (!TextUtils.isEmpty(getIntent().getStringExtra("end"))){
			popup_roder_dateil_text_end.setText("目的地("+getIntent().getStringExtra("end")+")");
		}
		if (!TextUtils.isEmpty(getIntent().getStringExtra("end_address"))){
			popup_roder_dateil_text_end_address.setText(getIntent().getStringExtra("end_address"));
		}
		if (!TextUtils.isEmpty(getIntent().getStringExtra("parcelNO"))){
			popup_roder_dateil_text_dd.setText("订单号:"+getIntent().getStringExtra("parcelNO"));
		}
		if (!TextUtils.isEmpty(getIntent().getStringExtra("end_money"))){
			popup_roder_dateil_text_yf.setText("配送费:"+getIntent().getStringExtra("end_money")+"元");
		}
		if (!TextUtils.isEmpty(des)){
			popup_roder_dateil_text_time.setText("配送时间:"+des);
		}else {
			popup_roder_dateil_text_time.setText("配送时间:"+getIntent().getStringExtra("end_time"));
		}
		if (!TextUtils.isEmpty(getIntent().getStringExtra("status"))){
			switch (Integer.parseInt(getIntent().getStringExtra("status"))){
				case 1:
					popup_roder_dateil_image_one.setSelected(true);
					popup_roder_dateil_image_two.setSelected(false);
					popup_roder_dateil_image_three.setSelected(false);
					popup_roder_dateil_text_one.setSelected(false);
					popup_roder_dateil_text_two.setSelected(false);
					break;
				case 2:
					popup_roder_dateil_text_start.setTextColor(getResources().getColor(R.color.colorBlackFour));
					popup_roder_dateil_text_address.setTextColor(getResources().getColor(R.color.colorBlackFour));
					popup_roder_dateil_image_one.setSelected(true);
					popup_roder_dateil_image_two.setSelected(true);
					popup_roder_dateil_image_three.setSelected(false);
					popup_roder_dateil_text_one.setSelected(true);
					popup_roder_dateil_text_two.setSelected(false);
					break;
				case 3:
					popup_roder_dateil_text_start.setTextColor(getResources().getColor(R.color.colorBlackFour));
					popup_roder_dateil_text_address.setTextColor(getResources().getColor(R.color.colorBlackFour));
					popup_roder_dateil_text_end.setTextColor(getResources().getColor(R.color.colorBlackFour));
					popup_roder_dateil_text_end_address.setTextColor(getResources().getColor(R.color.colorBlackFour));
					popup_roder_dateil_image_one.setSelected(true);
					popup_roder_dateil_image_two.setSelected(true);
					popup_roder_dateil_image_three.setSelected(true);
					popup_roder_dateil_text_one.setSelected(true);
					popup_roder_dateil_text_two.setSelected(true);
					break;
			}
		}

		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				popupWindow.dismiss();
			}
		});
		if (b){
			popupWindow.showAsDropDown(route_activity_title);
		}
	}
	/**
	 * 改变背景色
	 * @param v
	 */
	private void backgroundAlpha(float v) {
		WindowManager.LayoutParams ly = getWindow().getAttributes();
		ly.alpha = v;
		getWindow().setAttributes(ly);
	}
}

