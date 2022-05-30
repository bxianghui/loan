package com.bxx.loan.core.util;

/**
 * sha加密
 * @author : bu
 * @date : 2022/5/20  19:51
 */
public class SHAUtil {

    public static String encrypt(String src){
        return Utils.encrypt(src, "sha-1");
    }
}
