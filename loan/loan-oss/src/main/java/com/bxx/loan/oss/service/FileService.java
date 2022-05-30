package com.bxx.loan.oss.service;

import java.io.InputStream;

/**
 * @author : bu
 * @date : 2022/5/23  11:40
 */
public interface FileService {

    /**
     * 文件上传到阿里云oss
     */

    String upload(InputStream inputStream, String module, String fileName);

    void remove(String url);
}

