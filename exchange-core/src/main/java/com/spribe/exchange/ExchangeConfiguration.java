package com.spribe.exchange;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@ComponentScan(basePackages = {"com.spribe.exchange"})
@EnableJpaRepositories
@EnableJpaAuditing
@EnableScheduling
public class ExchangeConfiguration {

    @Bean
    ExecutorService executorService(@Value("${app.pool.size:20}") int poolSize) {
        return Executors.newFixedThreadPool(poolSize);
    }
}
