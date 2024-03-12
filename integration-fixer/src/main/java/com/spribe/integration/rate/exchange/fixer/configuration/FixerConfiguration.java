package com.spribe.integration.rate.exchange.fixer.configuration;

import com.spribe.integration.rate.exchange.fixer.FixerRateClient;
import com.spribe.integration.rate.exchange.fixer.properties.FixerProperties;
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
@EnableConfigurationProperties({FixerProperties.class})
@ConditionalOnProperty(name = "exchangeRateClient", havingValue = "fixer")
@ComponentScan(basePackages = "com.spribe.integration.rate.exchange.fixer")
public class FixerConfiguration {

    @Bean
    public FixerRateClient feignClient(FixerProperties properties) {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Slf4jLogger(FixerRateClient.class))
                .logLevel(Logger.Level.BASIC)
                .requestInterceptor(template -> template.query("access_key", properties.getApiKey()))
                .target(FixerRateClient.class, properties.getUrl());
    }
}
