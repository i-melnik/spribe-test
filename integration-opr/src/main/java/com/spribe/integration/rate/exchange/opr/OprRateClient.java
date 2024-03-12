package com.spribe.integration.rate.exchange.opr;

import com.spribe.integration.rate.exchange.opr.dto.OprRateResponse;
import feign.Param;
import feign.RequestLine;

import java.util.Map;

public interface OprRateClient {


    @RequestLine("GET /latest.json?base={base}")
    OprRateResponse getRates(@Param("base") String currency);

    @RequestLine("GET /currencies.json")
    Map<String, String> getCurrencies();

}
