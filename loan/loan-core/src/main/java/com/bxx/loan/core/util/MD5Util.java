package com.bxx.loan.core.util;

/**
 * MD5加密
 * @author : bu
 * @date : 2022/5/20  19:51
 */
public class MD5Util {
    public static String encryptMD5(String src){
        return Utils.encrypt(src, "MD5");
    }
}
