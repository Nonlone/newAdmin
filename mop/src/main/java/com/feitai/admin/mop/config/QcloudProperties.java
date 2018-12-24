package com.feitai.admin.mop.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ToString
public class QcloudProperties {

    @Value("${qcloud.cos.secretId}")
    private String cosSecretId;

    @Value("${qcloud.cos.secretKey}")
    private String cosSecretKey;

    private String apiGrantType = "client_credential";

    private String apiVersion = "1.0.0";

    @Value("${qcloud.cos.bucket.images}")
    private String cosBucketImages;

    @Value("${qcloud.cos.region}")
    private String cosRegion;

    @Bean
    public COSCredentials cosCredentials() {
        return new BasicCOSCredentials(cosSecretId, cosSecretKey);
    }

    @Bean(destroyMethod = "shutdown")
    public COSClient cosClient(COSCredentials cosCredentials) {
        ClientConfig clientConfig = new ClientConfig(new Region(cosRegion));
        clientConfig.setHttpProtocol(HttpProtocol.https);
        return new COSClient(cosCredentials, clientConfig);
    }
}
