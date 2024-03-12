package com.spribe.integration.rate.exchange.opr.properties;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("opr")
@Getter
@Setter
@Validated
public class OprProperties {

    @NotEmpty(message = "Api key for openexchangerates must be provided")
    private String apiKey;

    @NotEmpty(message = "Api url for openexchangerates must be provided")
    private String url;
}
