package com.stribe.integration.exchange.rate.api;


import com.stribe.integration.exchange.rate.dto.CurrencyRateDto;

import java.util.Map;

public interface ExchangeRateClient {

    CurrencyRateDto getRates(String currency);

    String getDefaultBaseCurrency();

    Map<String, String> getCurrencies();
}
