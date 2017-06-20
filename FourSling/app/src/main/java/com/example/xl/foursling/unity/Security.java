package com.example.xl.foursling.unity;

import java.util.List;

/**
 * Created by admin on 2016/12/29.
 */

public class Security {

    /**
     * code : 0
     * message : [{"id":10,"answerOne":"aaa","encryptedType1":1,"answerTwo":"bbb","encryptedType2":4,"answerThree":"cccc","encryptedType3":5,"userCode":"18292925988","createDate":1483003827000}]
     */

    public int code;
    public List<MessageBean> message;
    public static class MessageBean {
        /**
         * id : 10
         * answerOne : aaa
         * encryptedType1 : 1
         * answerTwo : bbb
         * encryptedType2 : 4
         * answerThree : cccc
         * encryptedType3 : 5
         * userCode : 18292925988
         * createDate : 1483003827000
         */

        public int id;
        public String answerOne;
        public int encryptedType1;
        public String answerTwo;
        public int encryptedType2;
        public String answerThree;
        public int encryptedType3;
        public String userCode;
        public long createDate;

    }
}
