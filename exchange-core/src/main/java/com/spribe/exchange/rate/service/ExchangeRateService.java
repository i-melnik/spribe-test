package com.spribe.exchange.rate.service;

import com.spribe.exchange.currency.repository.CurrencyRepository;
import com.spribe.exchange.rate.dto.ExchangeRateDto;
import com.spribe.exchange.rate.dto.RateCacheValue;
import com.spribe.exchange.rate.entity.ExchangeRate;
import com.spribe.exchange.rate.entity.ExchangeRateId;
import com.spribe.exchange.rate.repository.ExchangeRateRepository;
import com.stribe.integration.exchange.rate.api.ExchangeRateClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final ExchangeRateRepository repository;
    private final CurrencyRepository currencyRepository;
    private final ExchangeRateClient exchangeRateClient;
    private final Map<String, SortedSet<RateCacheValue>> cache = new ConcurrentHashMap<>();

    public List<ExchangeRateDto> getRates(String currency) {
        if (!cache.containsKey(currency)) {
            return List.of();
        }
        var rates = cache.get(currency);
        var newestRates = rates.first();
        return List.of(new ExchangeRateDto(currency, newestRates.rates(), newestRates.date()));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateRates(String code) {
        var currency = currencyRepository.getByCode(code);
        var defaultCurrencyRates = exchangeRateClient.getRates(code);
        var exchangeRates = ExchangeRate.builder()
                .id(new ExchangeRateId(null, defaultCurrencyRates.date()))
                .base(currency)
                .rates(defaultCurrencyRates.rates())
                .build();
        exchangeRates = repository.save(exchangeRates);
        updateCache(List.of(exchangeRates));
    }

    private void updateCache(List<ExchangeRate> rates) {
        rates.forEach(rate -> {
            var value = new RateCacheValue(rate.getId().getDate(), rate.getRates());
            var newSet = new TreeSet<>(Comparator.comparing(RateCacheValue::date).reversed());
            newSet.add(value);
            cache.merge(rate.getBase().getCode(), newSet, (newValue, existingValue) -> {
                existingValue.add(newValue.first());
                return existingValue;
            });
        });
    }

    @PostConstruct
    private void init() {
        var latestRates = repository.getLatestRates();
        updateCache(latestRates);
    }
}
