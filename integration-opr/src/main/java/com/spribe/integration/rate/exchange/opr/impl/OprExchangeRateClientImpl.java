package com.spribe.integration.rate.exchange.opr.impl;

import com.spribe.integration.rate.exchange.opr.OprRateClient;
import com.stribe.integration.exchange.rate.api.ExchangeRateClient;
import com.stribe.integration.exchange.rate.dto.CurrencyRateDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OprExchangeRateClientImpl implements ExchangeRateClient {

    private static final String DEFAULT_BASE = "USD";

    private final OprRateClient rateClient;

    @Override
    public CurrencyRateDto getRates(String currency) {
        try {
            var response = rateClient.getRates(currency);
            return new CurrencyRateDto(ZonedDateTime.ofInstant(Instant.ofEpochSecond(response.timestamp()), ZoneId.of("UTC")),
                                       response.base(),
                                       response.rates());
        } catch (FeignException.Forbidden e) {
            throw new RuntimeException("Obtaining rates for currency '%s' is not supported".formatted(currency), e);
        }
    }

    @Override
    public Map<String, String> getCurrencies() {
        return rateClient.getCurrencies();
    }

    @Override
    public String getDefaultBaseCurrency() {
        return DEFAULT_BASE;
    }
}
