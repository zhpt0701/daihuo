package com.example.xl.foursling.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xl on 2016/12/10.
 */

public class PhoneNumber {
    //手机号码正则判断
    public static boolean IsMoblieNo(String phone){
        Pattern p = Pattern.compile("^((13[0-9])|(14[0,5,7])|(15[0-9])|(17[0,1,3,5,6,7,8])|()|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }
    //判断运营商
    public static String Isyunying(String ph){
        Pattern py = Pattern.compile("^(134|135|136|137|138|139|147|150|151|157|158|159|178)[0-9]{8}$");
        Pattern pl = Pattern.compile("^(130|131|132|155|156|176)[0-9]{8}$");
        Pattern pd = Pattern.compile("^(133|153||180|181|189|177)[0-9]{8}$");
        if (py.matcher(ph).matches()){
            return "移动";
        }else if (pl.matcher(ph).matches()){
            return "联通";
        }else if (pd.matcher(ph).matches()){
            return "电信";
        }
        return null;
    }
    //邮编正则判断
    public static boolean Isyoubian(String youbian){
        String str = "^[1-9][0-9]{5}$";
        return Pattern.compile(str).matcher(youbian).matches();
    }
    //邮箱正则判断
    public static boolean Isemil(String email){
        if (null==email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
