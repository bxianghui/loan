package com.bxx.loan.core.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author : bu
 * @date : 2022/5/20  19:52
 */
public class Utils {
    public static  final Integer STATUS_NORMAL = 1;

    public static  final Integer STATUS_LOCKED = 0;

    public static final String USER_AVATAR = "https://loan-202205.oss-cn-hangzhou.aliyuncs.com/avatar/2022/05/23/85be180b-21d1-42cb-b305-74afdaf76e28.jpg";



    /**
     * 加密
     *
     * @param src
     * @param algorithmName
     * @return
     */
    public final static String encrypt(String src, String algorithmName) {
        if (src == null || "".equals(src.trim())) {
            throw new IllegalArgumentException("请输入要加密的内容");
        }
        if (algorithmName == null || "".equals(algorithmName.trim())) {
            algorithmName = "md5";
        }
        try {
            MessageDigest m = MessageDigest.getInstance(algorithmName);
            m.update(src.getBytes(StandardCharsets.UTF_8));
            byte[] s = m.digest();
            return hex(s);
        } catch (Exception e) {
            throw new RuntimeException("密码加密出现错误");
        }
    }

    /**
     * 返回十六进制字符串
     *
     * @param arr
     * @return
     */
    private final static String hex(byte[] arr) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; ++i) {
            sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100), 1, 3);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(SHAUtil.encrypt(MD5Util.encryptMD5("123456789")));
        System.out.println(SHAUtil.encrypt(MD5Util.encryptMD5("123456789")));//40位
        System.out.println(MD5Util.encryptMD5(SHAUtil.encrypt("123456789")));
        System.out.println(MD5Util.encryptMD5(SHAUtil.encrypt("123456789"))); //32位
    }
}
