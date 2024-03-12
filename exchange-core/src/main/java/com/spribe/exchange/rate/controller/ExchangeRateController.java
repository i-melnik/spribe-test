package com.spribe.exchange.rate.controller;

import com.spribe.exchange.rate.dto.ExchangeRateDto;
import com.spribe.exchange.rate.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ExchangeRateController.PATH)
@RequiredArgsConstructor
public class ExchangeRateController {

    public static final String PATH = "/api/v1/exchange-rates";
    private final ExchangeRateService service;

    @GetMapping
    public List<ExchangeRateDto> getRates(@RequestParam("currency") String currency) {
        return service.getRates(currency);
    }
}
