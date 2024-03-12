package com.spribe.exchange.currency.repository;

import com.spribe.exchange.TestConfiguration;
import com.spribe.exchange.TestcontainersInitializer;
import com.spribe.exchange.currency.entity.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import(TestConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = TestcontainersInitializer.class)
public class CurrencyRepositoryTest {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    public void test() {
        var currency = new Currency("CurrencyRepoTestCurrency", "Description");
        currency = currencyRepository.save(currency);

        assertEquals(currency, currencyRepository.getByCode(currency.getCode()));
    }
}
