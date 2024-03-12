package com.spribe.exchange;

import com.stribe.integration.exchange.rate.api.ExchangeRateClient;
import org.mockito.Mockito;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan("com.spribe.exchange")
public class TestConfiguration {

    @Bean
    ExchangeRateClient exchangeRateClient() {
        return Mockito.mock(ExchangeRateClient.class);
    }
}
