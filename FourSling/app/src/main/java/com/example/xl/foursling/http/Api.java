package com.example.xl.foursling.http;

/**
 * Created by xl on 2016/12/10.
 */
public class Api {
    public static String app_id = "2088512205816048";
    //商户支付宝帐号
    public static String seller = "2593405864@qq.com";
    public static String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKj0Hv/WW8T+P8eHerQY1p6MyNschV0yQrdUfMGPd2Rndf0lK9dxTEdxlJiKgZ5a44Avmv6POYHMKPBbrgMB3zXaiZ+zrxyKqI9u1BBsr8H+cBMejqXClwyio4f43bDxuX3thKVOMyA0ttB1rTPNFtaaOCG5KPggEdHK0cfZ8KAzAgMBAAECgYEAn/4VQPZQDvspkJx2EQgYbSoqpnuau0d2MD+8qFtgl172N5O0sZ8jueAP9GftGPU+zMvH/dnJfmXEOuDGpyhlowJ52TP6/B3+f95RnYedMnQ+OcxLwfyhwfpH09CNdXHgWcRh2HUvRMQAAc2vCWJvHIjF8qTYoWSqG7edRC718tkCQQDa45UYJSRbIyYTDDB/a/eXbxtNNSXetRIkvRTObC9/REzrXyDTfb0zEbqQtwf8RtbNIBNGgVyjLktV610fbhu1AkEAxZkylVnH+AmmiWjC/kkbgXqrzPRHoMdZaVJOHzjqRx4V+XLWL8q5tlccqR39PzUGKp0HAdw8p0qbU3aNdbvNRwJBANovLDirHTgzKwqlhdodlui9+WrWZW8+8lzolMrGD/o97KZZZnEpzuQO8fa6Wx1ex1IkfNI0tFB8HuAAAg0IRlkCQCR5J7YbsWaeWWgptzx6wCIBQ678NDCAO4fOUm8+8TeZXRMDWHfP4sG+iKLKz1EYd3/sJjv4rBVxFwSr8GaX+McCQQCxW6kYR6Yp9PiSZHgvukO2a1NFMrnsiAY47twtMZh/RPjmTglyuBFwmx2a3xJr0oU0bDNRGfwkmzzEXjjVVRPO";
    //测试地址
//    public static String IP = "http://localhost:8080/idaihuo-service3.0/";
    //柳博天
//    public static String IP = "http://172.22.2.68:8080/idaihuo-service3.0/";
//    public static String IP = "http://172.21.1.28:8080/idaihuo-service3.0/";
    //高丹放
//    public static String IP = "http://172.21.1.84:8080/idaihuo-service3.0/";
//    public static String IP = "http://172.22.4.36:8080/idaihuo-service3.0/";
    //外网测试地址
//    public static String IP = "http://idaihuo.mastershao.cn/idaihuo-service3.0/";
    //孙斌
//    public static String IP ="http://172.22.3.51:8080/idaihuo-service3.0/";
//    线上
    public static String IP = "http://idaihuo.xilaikd.com/idaihuo-service3.0/";
    //登陆接口
    public static String landing = IP + "iUserController/login";
    //注册接口
    public static String register = IP + "iUserController/registered";
    //验证码
    public static String auth_code = IP + "iUserController/sendMes";
    //密码找回
    public static String pass = IP + "iUserController/forgotPWD";
    //用户查询
    public static String select_user = IP + "iUserController/selectUser";
    //查找是否设置支付密码
    public static String select_pay_pass = IP + "account/checkPayPwd";
    //设置支付密码
    public static String setting_pay_pass = IP + "account/setPwd";
    //手机号修改支付和登录密码
    public static String phone_update_pay_pass = IP + "account/resetByPwd";
    //通过旧密码修改新支付密码
    public static String old_pass_update_pay_pass = IP + "account/restPwd";
    //通过旧密码修改新登陆密码
    public static String old_pass_upate_landing_pass = IP + "iUserController/rememberPwd";
    //添加个人信息
    public static String add_this_news = IP + "userInformation/addUserInformation";
    //查找用户是否审核
    public static String select_user_audit = IP + "userInformation/findUserStatus";
    //修改用户图像
    public static String update_photo = IP + "iUserController/updateUserPic";
    //修改用户性别
    public static String update_sex = IP + "iUserController/updateUserSex";
    //添加车辆
    public static String add_car = IP + "userCar/addCar";
    //查询所有车辆
    public static String select_all_car = IP + "userCar/findAllUserCar";
    //通过电话查询正在使用的car
    public static String select_phone_car = IP + "userCar/findUsingCar";
    //设置正在使用的车辆
    public static String this_car = IP + "userCar/updateCar";
    //注销账号
    public static String cancel_user = IP + "iUserController/cancelUser";
    //修改常用地址
    public static String update_address = IP + "userInformation/updateAddress";
    //今日收入金额
    public static String today_income = IP + "account/todayIncome";
    //今日支出金额
    public static String today_deposit = IP + "account/todaySpending";
    //今日收入明细
    public static String today_income_detail = IP + "account/todayIncomeRecord";
    //今日支出明细
    public static String today_deposit_detail = IP + "account/todaySpendingRecord";
    //提现明细
    public static String deposit_detail = IP + "account/payCashRecord";
    //充值明细
    public static String recharge_detail = IP + "account/RechargeRecord";
    //查找账户余额
    public static String select_balance = IP+"account/findbalance";
    //提现
    public static String deposit = IP+"account/payCash";
    //通过单号查询充值详情
    public static String recharge_select = IP+"account/RechargeOrderCode";
    //通过单号查询提现详情
    public static String deposit_select = IP+"account/payCashOrderCode";
    //系统通知
    public static String system_message = IP+"message/allMessage";
    //删除系统通知
    public static String delete_message = IP+"message/updateMessage";
    //系统通知阅读状态
    public static String readercode = IP+"message/readCode";
    //钱包通知
    public static String money_message = IP+"message/findUserPay";
    //带货运费通知
    public static String four_sling_message = IP+"message/selectSettlementMessage";
    // 接单通知
    public static String oder_message = IP+"message/selectOrderMessage";
    //查找某一个单号的进度
    public static String vity_plan = IP+"message/findOrderCode";
    //设置密保问题
    public static String setting_security = IP+"iUserController/addAnswer";
    //验证密保问题
    public static String verift_security = IP+"iUserController/answerVerification";
    //查找密保问题是否存在
    public static String select_security = IP+"iUserController/queryAnswer";
    //身份验证
    public static String ID_verity = IP+"userInformation/idCardVerification";
    //微信充值
    public static String Whcat_wx = IP+"account/wechatAccountCharge";
    //支付宝充值
    public static String alipay_code = IP+"account/doCharge";
    //充值成功回掉http
    public static String resulter = "http://idaihuo.xilaikd.com/idaihuo-service3.0/account/checkPayStatus";
    //接单详情
    public static String select_roder_dateil =IP+"distributionOrder/findOrder";
    //接单数据
    public static String select_all_order = IP+"parcelOrderController/findAllOrder";
    //接单
    public static String receiving_order = IP+"parcelOrderController/receiveOrder";
    //取消订单
    public static String cancel_order = IP+"parcelOrderController/cancelOrder";
    //查找订单
    public static String select_order = IP+"parcelOrderController/selectSpecifiedOrder";
    //行程列表数据
    public static String select_all_route = IP+"parcelOrderController/selectTrip";
    //行程已完成
    public static String select_all_over_route = IP+"parcelOrderController/selectTripOver";
    //消息
    public static String select_message = IP+"message/selectMessageStatus";
    //查询个人资料
    public static String select_user_data = IP+"userInformation/selectUserData";
    //版本更新
//    public static String version_update = IP+"basisVersionController/idhUpdate";
    //
    public static String version_update = IP+"basisVersionController/getUpdate";

    //接单判断个人车辆审核状态
    public static String roder_state = IP+"parcelOrderController/status";
    //意见反馈
    public static String Opinion = "http://manage.xilaikd.com/ixilai-guanli/applyPerson/saveIksMessage";
}
