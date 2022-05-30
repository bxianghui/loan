package com.bxx.loan.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import com.aliyun.oss.model.CannedAccessControlList;
import com.bxx.loan.oss.service.FileService;
import com.bxx.loan.oss.util.OssProperties;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

/**
 * @author : bu
 * @date : 2022/5/23  11:41
 */
@Service
public class FileServiceImpl implements FileService {



    @Override
    public String upload(InputStream inputStream, String module, String fileName) {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(OssProperties.ENDPOINT, OssProperties.KEY_ID, OssProperties.KEY_SECRET);

        //判断BUCKET_NAME存在
        if (!ossClient.doesBucketExist(OssProperties.BUCKET_NAME)) {
            ossClient.createBucket(OssProperties.BUCKET_NAME);
            ossClient.setBucketAcl(OssProperties.BUCKET_NAME, CannedAccessControlList.PublicRead);
        }
        //              avatar/2022/5/23/uuid.jpg
        String folder = new DateTime().toString("/yyyy/MM/dd/");
        fileName = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."));
        String key = module + folder + fileName;
        // 创建PutObject请求。
        ossClient.putObject(OssProperties.BUCKET_NAME, key, inputStream);
        ossClient.shutdown();

        return "https://" + OssProperties.BUCKET_NAME + "." + OssProperties.ENDPOINT + "/" + key;
    }

    @Override
    public void remove(String url) {
        OSS ossClient = new OSSClientBuilder().build(OssProperties.ENDPOINT, OssProperties.KEY_ID, OssProperties.KEY_SECRET);

        //解析url获取文件名
        String host = "https://" + OssProperties.BUCKET_NAME + "." + OssProperties.ENDPOINT + "/";
        String ObjectName = url.substring(host.length());
        ossClient.deleteObject(OssProperties.BUCKET_NAME, ObjectName);

        ossClient.shutdown();
    }
}
