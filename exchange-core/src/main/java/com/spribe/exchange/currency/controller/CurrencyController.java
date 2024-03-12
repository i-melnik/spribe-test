package com.spribe.exchange.currency.controller;

import com.spribe.exchange.currency.dto.CurrencyDto;
import com.spribe.exchange.currency.service.CurrencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(CurrencyController.PATH)
@RequiredArgsConstructor
public class CurrencyController {

    public static final String PATH = "/api/v1/currencies";

    private final CurrencyService service;

    @GetMapping
    public List<CurrencyDto> getCurrencies() {
        return service.getAll();
    }

    @PostMapping
    public CurrencyDto createCurrency(@RequestBody @Valid CurrencyDto createRequest) {
        return service.create(createRequest);
    }
}
