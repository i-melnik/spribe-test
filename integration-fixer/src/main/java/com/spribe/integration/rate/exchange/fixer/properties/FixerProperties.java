package com.spribe.integration.rate.exchange.fixer.properties;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("fixer")
@Getter
@Setter
@Validated
public class FixerProperties {

    @NotEmpty(message = "Api key must be provided")
    private String apiKey;

    @NotEmpty(message = "Api url msu be provided")
    private String url;
}
