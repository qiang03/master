package com.xue.im.client.config;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import okhttp3.OkHttpClient;

/**
 * @author 小五老师-云析学院
 * @createTime 2019年3月12日 下午9:36:36
 * 
 */
@Configuration
public class BeanConfiguration {
	
    /**
     * http client
     * @return okHttp
     */
    @Bean
	public OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
		return builder.build();
	}
}
