package com.example.xl.foursling.tools.alipay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.example.xl.foursling.http.Api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class AliSinglePay {
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SDK_PAY_FLAG: {
					PayResult payResult = new PayResult((String) msg.obj);
					// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
					String resultInfo = payResult.getResult();
					Log.i("resultInfo:", resultInfo);
					String resultStatus = payResult.getResultStatus();
					Log.i("resultStatus:", resultStatus);
					// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
					if (TextUtils.equals(resultStatus, "9000")) {
						callBack.payResult(Integer.parseInt(resultStatus),"支付成功",sdkVersion);
					} else {
						// 判断resultStatus 为非“9000”则代表可能支付失败
						// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
						if (TextUtils.equals(resultStatus, "8000")) {
							callBack.payResult(Integer.parseInt(resultStatus),"支付结果确认中",sdkVersion);
						} else {
							// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
							callBack.payResult(Integer.parseInt(resultStatus),"支付失败",sdkVersion);
						}
					}
					break;
				}
				case SDK_CHECK_FLAG: {
					callBack.checkAliPayUser(Boolean.parseBoolean(String.valueOf(msg.obj)));
					break;
				}
			}
		};
	};

	public interface CallBack {
		/**
		 * 商品订单编号
		 */
		public String commodityOrderId();
		/**
		 * 商品名称
		 * 
		 * @return
		 */
		public String commodityName();

		/**
		 * 商品备注
		 * 
		 * @return
		 */
		public String commodityRemarks();

		/**
		 * 商品总价
		 * 
		 * @return
		 */
		public Integer commodityNumber();

		/**
		 * 商品单价
		 * 
		 * @return
		 */
		public Double commodityUnitPrice();

		/**
		 * 查询终端设备是否存在支付宝认证账户
		 * 
		 * @return
		 */
		public void checkAliPayUser(boolean isExist);

		/**
		 * 支付结果
		 */
		public void payResult(int resultCode, String resultMessage, String sdkVersion);
	}

	private Context context;
	private CallBack callBack;
	private String name;
	private String remarks;
	private Integer number;
	private String orderId;
	private Double unitPrice;
	private String sdkVersion;

	public AliSinglePay(Context context, final CallBack callBack) {
		if (context == null) {
			throw new IllegalArgumentException("Parameter context is empty.");
		} else {
			this.context = context;
		}
		if(callBack == null){
			throw new IllegalArgumentException("Parameter callBack is empty.");
		}else{
			this.callBack = callBack;
		}
		this.name = callBack.commodityName() == null ? "":callBack.commodityName();
		this.remarks = callBack.commodityRemarks() == null ? "":callBack.commodityRemarks();
		this.orderId = callBack.commodityOrderId();
		this.number = callBack.commodityNumber();
		this.unitPrice = callBack.commodityUnitPrice();
		if(orderId == null){
			throw new IllegalArgumentException("Parameter Order number is empty.");
		}
		if(number == null){
			throw new IllegalArgumentException("Parameter commodity number is empty.");
		}
		if(unitPrice == null){
			throw new IllegalArgumentException("Parameter commodity unitPrice is empty.");
		}
		//0.获得版本编号
		this.sdkVersion = getSDKVersion();
		//1.校验终端设备是否存在支付宝认证账户
		check();
		//2.支付
		pay();
	}

	// 商户PID
	private static final String PARTNER = "2088512205816048";
	// 商户收款账号
	private static final String SELLER = "2593405864@qq.com";
	// 商户私钥，pkcs8格式
	private static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKj0Hv/WW8T+P8eHerQY1p6MyNschV0yQrdUfMGPd2Rndf0lK9dxTEdxlJiKgZ5a44Avmv6POYHMKPBbrgMB3zXaiZ+zrxyKqI9u1BBsr8H+cBMejqXClwyio4f43bDxuX3thKVOMyA0ttB1rTPNFtaaOCG5KPggEdHK0cfZ8KAzAgMBAAECgYEAn/4VQPZQDvspkJx2EQgYbSoqpnuau0d2MD+8qFtgl172N5O0sZ8jueAP9GftGPU+zMvH/dnJfmXEOuDGpyhlowJ52TP6/B3+f95RnYedMnQ+OcxLwfyhwfpH09CNdXHgWcRh2HUvRMQAAc2vCWJvHIjF8qTYoWSqG7edRC718tkCQQDa45UYJSRbIyYTDDB/a/eXbxtNNSXetRIkvRTObC9/REzrXyDTfb0zEbqQtwf8RtbNIBNGgVyjLktV610fbhu1AkEAxZkylVnH+AmmiWjC/kkbgXqrzPRHoMdZaVJOHzjqRx4V+XLWL8q5tlccqR39PzUGKp0HAdw8p0qbU3aNdbvNRwJBANovLDirHTgzKwqlhdodlui9+WrWZW8+8lzolMrGD/o97KZZZnEpzuQO8fa6Wx1ex1IkfNI0tFB8HuAAAg0IRlkCQCR5J7YbsWaeWWgptzx6wCIBQ678NDCAO4fOUm8+8TeZXRMDWHfP4sG+iKLKz1EYd3/sJjv4rBVxFwSr8GaX+McCQQCxW6kYR6Yp9PiSZHgvukO2a1NFMrnsiAY47twtMZh/RPjmTglyuBFwmx2a3xJr0oU0bDNRGfwkmzzEXjjVVRPO";
	// 支付宝公钥
	private static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;
	
	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	private void pay() {
		String orderInfo = getOrderInfo(name, remarks, String.valueOf(number*unitPrice));
		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask((Activity)context);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}



	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	private void check() {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask((Activity)context);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();
				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	private String getSDKVersion() {
		PayTask payTask = new PayTask((Activity)context);
		return payTask.getVersion();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	private String getOrderInfo(String subject, String body, String price) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + callBack.commodityOrderId() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + Api.resulter+ "\"";
//		orderInfo += "&notify_url=" + "\"" + "http://ikuaidi.mastershao.cn/ixilai-service/PartorderController/rechargeSucess" + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	private String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);
		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	private String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	
}
