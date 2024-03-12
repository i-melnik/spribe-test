package com.stribe.integration.exchange.rate.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;

public record CurrencyRateDto(ZonedDateTime date, String base, Map<String, BigDecimal> rates) {
}
