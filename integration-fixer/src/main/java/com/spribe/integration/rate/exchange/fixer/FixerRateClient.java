package com.spribe.integration.rate.exchange.fixer;

import com.stribe.integration.exchange.rate.api.ExchangeRateClient;
import com.stribe.integration.exchange.rate.dto.CurrencyRateDto;
import feign.Param;
import feign.RequestLine;

public interface FixerRateClient extends ExchangeRateClient {


    @Override
    @RequestLine("GET ?base={base}")
    CurrencyRateDto getRates(@Param("base") String currency);
}
