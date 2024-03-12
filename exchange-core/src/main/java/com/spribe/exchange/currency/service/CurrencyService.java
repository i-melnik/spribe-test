package com.spribe.exchange.currency.service;

import com.spribe.exchange.currency.dto.CurrencyDto;
import com.spribe.exchange.currency.mapper.CurrencyMapper;
import com.spribe.exchange.currency.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository repository;
    private final CurrencyMapper mapper;

    @Transactional(readOnly = true)
    public List<CurrencyDto> getAll() {
        return repository.findAll().stream().map(mapper::map).toList();
    }

    @Transactional
    public CurrencyDto create(CurrencyDto dto) {
        var currencyOptional = repository.findByCode(dto.code());
        if (currencyOptional.isPresent()) {
            return mapper.map(currencyOptional.get());
        }
        var currency = repository.save(mapper.map(dto));
        return mapper.map(currency);
    }
}
