package com.spribe.exchange.rate.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;

public record RateCacheValue(ZonedDateTime date, Map<String, BigDecimal> rates) {
}
