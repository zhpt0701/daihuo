package com.example.xl.foursling.wxapi;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);

    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}
	@Override
	public void onReq(BaseReq req) {
		Log.d("this_weixin","onPayFinish, errCode = "+ req.openId);
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d("this_weixin",resp.getType()+"onPayFinish, errCode = "+ resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle(R.string.app_tip);
			switch (resp.errCode){
				case 0:
					Toast.makeText(getApplication(),getString(R.string.pay_result_callback_cg),Toast.LENGTH_LONG).show();
//					builder.setMessage(getString(R.string.pay_result_callback_msg,getString(R.string.pay_result_callback_cg)));
					break;
				case -1:
					Toast.makeText(getApplication(),getString(R.string.pay_result_callback_sb),Toast.LENGTH_LONG).show();
//					builder.setMessage(getString(R.string.pay_result_callback_msg,getString(R.string.pay_result_callback_sb)));
					break;
				case -2:
					Toast.makeText(getApplication(),getString(R.string.pay_result_callback_qx),Toast.LENGTH_LONG).show();
//					builder.setMessage(getString(R.string.pay_result_callback_msg,getString(R.string.pay_result_callback_qx)));
					break;
			}
//			builder.show();
			finish();
		}
	}
}