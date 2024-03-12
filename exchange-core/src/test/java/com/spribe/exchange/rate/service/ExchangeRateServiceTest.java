package com.spribe.exchange.rate.service;

import com.spribe.exchange.TestConfiguration;
import com.spribe.exchange.TestcontainersInitializer;
import com.spribe.exchange.currency.entity.Currency;
import com.spribe.exchange.currency.repository.CurrencyRepository;
import com.spribe.exchange.rate.repository.ExchangeRateRepository;
import com.stribe.integration.exchange.rate.api.ExchangeRateClient;
import com.stribe.integration.exchange.rate.dto.CurrencyRateDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = TestConfiguration.class)
@ContextConfiguration(initializers = TestcontainersInitializer.class)
public class ExchangeRateServiceTest {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private ExchangeRateClient exchangeRateClient;

    @Test
    public void testGetRates() {
        var rates = exchangeRateService.getRates("USD");
        assertEquals(0, rates.size());
    }

    @Test
    public void testUpdateRates() {
        String currencyCode = "USD";
        var usdCurrency = new Currency(currencyCode, "United State Dollar");
        usdCurrency = currencyRepository.save(usdCurrency);

        var date = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Z"));
        var rateDto = new CurrencyRateDto(date, currencyCode, Map.of("EUR", new BigDecimal("1.2"), "AED", new BigDecimal("4.1")));
        Mockito.when(exchangeRateClient.getRates(Mockito.eq(currencyCode))).thenReturn(rateDto);

        exchangeRateService.updateRates(currencyCode);

        var savedRates = exchangeRateRepository.findAll();
        assertEquals(1, savedRates.size());

        var cachedRates = exchangeRateService.getRates(currencyCode);
        assertNotNull(cachedRates);
        assertEquals(1, cachedRates.size());
        var cachedRate = cachedRates.get(0);
        assertEquals(cachedRate.base(), currencyCode);
        assertEquals(0, date.truncatedTo(ChronoUnit.MILLIS).compareTo(cachedRate.date().truncatedTo(ChronoUnit.MILLIS)));
        assertEquals(2, cachedRate.rates().entrySet().size());
        assertTrue(cachedRate.rates()
                           .entrySet()
                           .stream()
                           .anyMatch(e -> "EUR".equals(e.getKey()) && new BigDecimal("1.2").compareTo(e.getValue()) == 0));
        assertTrue(cachedRate.rates()
                           .entrySet()
                           .stream()
                           .anyMatch(e -> "AED".equals(e.getKey()) && new BigDecimal("4.1").compareTo(e.getValue()) == 0));

        exchangeRateRepository.deleteAll();
        currencyRepository.delete(usdCurrency);
    }
}
