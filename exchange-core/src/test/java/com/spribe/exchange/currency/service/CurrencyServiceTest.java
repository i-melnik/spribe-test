package com.spribe.exchange.currency.service;

import com.spribe.exchange.TestConfiguration;
import com.spribe.exchange.TestcontainersInitializer;
import com.spribe.exchange.currency.dto.CurrencyDto;
import com.spribe.exchange.currency.entity.Currency;
import com.spribe.exchange.currency.repository.CurrencyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = TestConfiguration.class)
@ContextConfiguration(initializers = TestcontainersInitializer.class)
public class CurrencyServiceTest {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CurrencyService currencyService;

    @Test
    public void testLoadCurrencies() {
        var allCurrency = new Currency("ALL", "All desc");
        var usdCurrency = new Currency("USD", "United State Dollar");
        List<Currency> currencies = List.of(allCurrency, usdCurrency);
        currencies = currencyRepository.saveAll(currencies);

        var loadedCurrencies = currencyService.getAll();
        assertEquals(2, loadedCurrencies.size());

        var allDto = loadedCurrencies.stream().filter(c -> "ALL".equals(c.code())).findFirst().orElse(null);
        var usdDto = loadedCurrencies.stream().filter(c -> "USD".equals(c.code())).findFirst().orElse(null);
        assertNotNull(allDto);
        assertEquals(allCurrency.getCode(), allDto.code());
        assertEquals(allCurrency.getName(), allDto.description());

        assertNotNull(usdDto);
        assertEquals(usdCurrency.getCode(), usdDto.code());
        assertEquals(usdCurrency.getName(), usdDto.description());

        currencyRepository.deleteAll(currencies);
    }

    @Test
    public void testCreateCurrency() {
        var usdCurrency = new CurrencyDto("USD", "United State Dollar");
        var created = currencyService.create(usdCurrency);
        assertNotNull(created);
        assertEquals(usdCurrency, created);

        currencyRepository.deleteById(usdCurrency.code());
    }

    @Test
    public void testCreateDuplicateCurrency() {
        var usdCurrency = new CurrencyDto("USD", "United State Dollar");
        currencyService.create(usdCurrency);
        currencyService.create(usdCurrency);

        var usdCurrencies = currencyRepository.findAllById(List.of(usdCurrency.code()));
        assertNotNull(usdCurrencies);
        assertEquals(1, usdCurrencies.size());

        currencyRepository.deleteById(usdCurrency.code());
    }
}
