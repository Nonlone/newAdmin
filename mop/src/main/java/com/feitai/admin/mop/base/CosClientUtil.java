package com.feitai.admin.mop.base;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 把文件上传到腾讯云工具类
 *
 * @autor zhengjianhua
 */
@Slf4j
@Component
public class CosClientUtil {

    static class FileUploadException extends Exception {
        public FileUploadException(String message) {
            super(message);
        }
    }

    @Autowired
    private COSClient cosClient;

    /**
     * 上传图片
     *
     * @param url
     */
    public void uploadImg2Cos(String url, String bucketName) throws FileUploadException {
        File fileOnServer = new File(url);
        FileInputStream fin;
        try {
            fin = new FileInputStream(fileOnServer);
            String[] split = url.split("/");
            this.uploadFile2Cos(fin, split[split.length - 1], bucketName);
        } catch (FileNotFoundException e) {
            throw new FileUploadException("图片上传失败");
        }
    }

    public String uploadFile2Cos(MultipartFile file, String bucketName) throws FileUploadException {
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new FileUploadException("上传图片大小不能超过10M！");
        }
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
        Random random = ThreadLocalRandom.current();
        String name = random.nextInt(10000) + System.currentTimeMillis() + substring;
        try {
            InputStream inputStream = file.getInputStream();
            this.uploadFile2Cos(inputStream, name, bucketName);
            return name;
        } catch (Exception e) {
            throw new FileUploadException("图片上传失败");
        }
    }

    /**
     * 获得url链接
     *
     * @param key
     * @return
     */
    public String getUrl(String bucketName, String key) {
        // 设置URL过期时间为10年 3600l* 1000*24*365*10
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = cosClient.generatePresignedUrl(bucketName, key, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }

    /**
     * 上传到COS服务器 如果同名文件会覆盖服务器上的
     *
     * @param instream
     *            文件流
     * @param fileName
     *            文件名称 包括后缀名
     * @return 出错返回"" ,唯一MD5数字签名
     */
    public String uploadFile2Cos(InputStream instream, String fileName, String bucketName) {
        String ret = "";
        try {
            // 创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getContentType(fileName.substring(fileName.lastIndexOf('.'))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            // 上传文件
            PutObjectResult putResult = cosClient.putObject(bucketName,  fileName, instream, objectMetadata);
            ret = putResult.getETag();
        } catch (IOException e) {
            log.error("上传文件出错", e);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                log.error("关闭文件流出错", e);
            }
        }
        return ret;
    }

    /**
     * Description: 判断Cos服务文件上传时文件的contentType
     *
     * @param filenameExtension 文件后缀
     * @return String
     */
    public String getContentType(String filenameExtension) {
        if ("bmp".equalsIgnoreCase(filenameExtension)) {
            return "image/bmp";
        }
        if ("gif".equalsIgnoreCase(filenameExtension)) {
            return "image/gif";
        }
        if ("jpeg".equalsIgnoreCase(filenameExtension) || "jpg".equalsIgnoreCase(filenameExtension)
                || "png".equalsIgnoreCase(filenameExtension)) {
            return "image/jpeg";
        }
        if ("html".equalsIgnoreCase(filenameExtension)) {
            return "text/html";
        }
        if ("txt".equalsIgnoreCase(filenameExtension)) {
            return "text/plain";
        }
        if ("vsd".equalsIgnoreCase(filenameExtension)) {
            return "application/vnd.visio";
        }
        if ("pptx".equalsIgnoreCase(filenameExtension) || "ppt".equalsIgnoreCase(filenameExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if ("docx".equalsIgnoreCase(filenameExtension) || "doc".equalsIgnoreCase(filenameExtension)) {
            return "application/msword";
        }
        if ("xml".equalsIgnoreCase(filenameExtension)) {
            return "text/xml";
        }
        return "image/jpeg";
    }

    /**
     * 把文件上传到腾讯云
     * @param cosFile 上传的文件
     * @param key 上传的路径
     * @return 返回上传的后的下载地址url
     * @throws FileUploadException 异常
     */
    public String picCOS(File cosFile, String key, String bucketName) throws FileUploadException {
        // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用该接口
        // 大文件上传请参照 API 文档高级 API 上传
        // 指定要上传到 COS 上的路径
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, cosFile);
        cosClient.putObject(putObjectRequest);
        cosClient.shutdown();
        Date expiration = new Date(System.currentTimeMillis() + 5 * 60 * 10000);
        URL url = cosClient.generatePresignedUrl(bucketName, key, expiration);
        return url.toString();
    }


    /***
     * 根据url获取流
     * @param cosPath
     * @return
     * @throws IOException
     */
    public byte[] getBytes(String cosPath) throws IOException {
        String key = cosPath.substring(cosPath.lastIndexOf("com/") + 4, cosPath.length());
        String bucketName = cosPath.substring(cosPath.indexOf("//") + 2, cosPath.indexOf(".cos"));
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        COSObject cosObject = cosClient.getObject(getObjectRequest);
        try(
                COSObjectInputStream in = cosObject.getObjectContent();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ){
            IOUtils.copy(in,baos);
            return baos.toByteArray();
        }
    }

}
