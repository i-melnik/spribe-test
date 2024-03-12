package com.spribe.exchange.currency.dto;

import jakarta.validation.constraints.NotEmpty;

public record CurrencyDto(@NotEmpty(message = "Currency code must be present") String code,
                          String description) {
}
