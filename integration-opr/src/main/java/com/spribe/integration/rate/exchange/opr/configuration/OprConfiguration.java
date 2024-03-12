package com.spribe.integration.rate.exchange.opr.configuration;

import com.spribe.integration.rate.exchange.opr.OprRateClient;
import com.spribe.integration.rate.exchange.opr.properties.OprProperties;
import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({OprProperties.class})
@ConditionalOnProperty(name = "exchangeRateClient", havingValue = "opr")
@ComponentScan(basePackages = "com.spribe.integration.rate.exchange.opr")
public class OprConfiguration {

    @Bean
    public OprRateClient feignClient(OprProperties properties) {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Slf4jLogger(OprRateClient.class))
                .logLevel(Logger.Level.BASIC)
                .requestInterceptor(template -> template.query("app_id", properties.getApiKey()))
                .target(OprRateClient.class, properties.getUrl());
    }
}
