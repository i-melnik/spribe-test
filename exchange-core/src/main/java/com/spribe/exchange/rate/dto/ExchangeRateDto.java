package com.spribe.exchange.rate.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;

public record ExchangeRateDto(String base, Map<String, BigDecimal> rates, ZonedDateTime date) {
}
