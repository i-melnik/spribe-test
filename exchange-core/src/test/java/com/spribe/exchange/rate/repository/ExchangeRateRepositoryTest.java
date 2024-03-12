package com.spribe.exchange.rate.repository;

import com.spribe.exchange.TestcontainersInitializer;
import com.spribe.exchange.currency.entity.Currency;
import com.spribe.exchange.currency.repository.CurrencyRepository;
import com.spribe.exchange.rate.entity.ExchangeRate;
import com.spribe.exchange.rate.entity.ExchangeRateId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = TestcontainersInitializer.class)
public class ExchangeRateRepositoryTest {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    public void testInsert() {
        var currency = new Currency("CurrencyRepoTestCurrency", "Description");
        currency = currencyRepository.save(currency);

        var rate = ExchangeRate.builder().base(currency).id(new ExchangeRateId(null, ZonedDateTime.now())).rates(
                Map.of("ALL", new BigDecimal(100))).build();

        rate = exchangeRateRepository.save(rate);
        assertNotNull(rate);

        var savedRate = exchangeRateRepository.getReferenceById(rate.getId());
        assertEquals(rate, savedRate);

        exchangeRateRepository.delete(rate);
        currencyRepository.delete(currency);
    }

    @Test
    public void testInsertDuplicate() {
        var currency = new Currency("CurrencyRepoTestCurrency", "Description");
        currency = currencyRepository.save(currency);

        var date = ZonedDateTime.now();
        var rate = ExchangeRate.builder().base(currency).id(new ExchangeRateId(null, date)).rates(
                Map.of("ALL", new BigDecimal(100))).build();

        var rate2 = ExchangeRate.builder().base(currency).id(new ExchangeRateId(null, date)).rates(
                Map.of("USD", new BigDecimal(100))).build();

        rate = exchangeRateRepository.save(rate);
        assertNotNull(rate);

        assertThrows(DuplicateKeyException.class, () -> exchangeRateRepository.save(rate2));

        exchangeRateRepository.delete(rate);
        currencyRepository.delete(currency);
    }

    @Test
    public void testGetLatestRates() {
        var currency = new Currency("CurrencyRepoTestCurrency", "Description");
        var usdCurrency = new Currency("USD", "usd");
        currency = currencyRepository.save(currency);
        usdCurrency = currencyRepository.save(usdCurrency);

        var date = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Z"));
        var newDate = date.plusHours(1);
        var oldRateUsd = ExchangeRate.builder().base(usdCurrency).id(new ExchangeRateId(null, date)).rates(
                Map.of("ALL", new BigDecimal(100))).build();
        var newRateUsd = ExchangeRate.builder().base(usdCurrency).id(new ExchangeRateId(null, newDate)).rates(
                Map.of("ALL", new BigDecimal(100))).build();
        var oldRate = ExchangeRate.builder().base(currency).id(new ExchangeRateId(null, date)).rates(
                Map.of("ALL", new BigDecimal(100))).build();
        var newRate = ExchangeRate.builder().base(currency).id(new ExchangeRateId(null, newDate)).rates(
                Map.of("ALL", new BigDecimal(100))).build();

        exchangeRateRepository.saveAll(List.of(oldRate, oldRateUsd, newRate, newRateUsd));

        var rates = exchangeRateRepository.findAll();
        assertEquals(4, rates.size());

        var latestRates = exchangeRateRepository.getLatestRates();
        assertEquals(2, latestRates.size());

        Currency finalUsdCurrency = usdCurrency;
        var expectedNewUsdRate = latestRates.stream()
                .filter(r -> r.getBase().getCode().equals(finalUsdCurrency.getCode()))
                .findFirst()
                .orElse(null);
        assertNotNull(expectedNewUsdRate);
        assertEquals(0,
                     newDate.truncatedTo(ChronoUnit.MILLIS)
                             .compareTo(expectedNewUsdRate.getId().getDate().truncatedTo(ChronoUnit.MILLIS)));

        Currency finalCurrency = currency;
        var expectedNewRate = latestRates.stream()
                .filter(r -> r.getBase().getCode().equals(finalCurrency.getCode()))
                .findFirst()
                .orElse(null);

        assertNotNull(expectedNewRate);
        assertEquals(0, newDate.truncatedTo(ChronoUnit.MILLIS).compareTo(expectedNewRate.getId().getDate().truncatedTo(ChronoUnit.MILLIS)));

        exchangeRateRepository.deleteAll(rates);
        currencyRepository.delete(currency);
        currencyRepository.delete(usdCurrency);
    }
}
