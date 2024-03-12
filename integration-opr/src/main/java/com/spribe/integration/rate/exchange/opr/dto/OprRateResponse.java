package com.spribe.integration.rate.exchange.opr.dto;

import java.math.BigDecimal;
import java.util.Map;

public record OprRateResponse(Long timestamp, String base, Map<String, BigDecimal> rates) {
}
