package com.spribe.exchange.rate.controller;

import com.spribe.exchange.TestConfiguration;
import com.spribe.exchange.TestcontainersInitializer;
import com.spribe.exchange.currency.entity.Currency;
import com.spribe.exchange.currency.repository.CurrencyRepository;
import com.spribe.exchange.rate.repository.ExchangeRateRepository;
import com.spribe.exchange.rate.service.ExchangeRateService;
import com.stribe.integration.exchange.rate.api.ExchangeRateClient;
import com.stribe.integration.exchange.rate.dto.CurrencyRateDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfiguration.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@ContextConfiguration(initializers = TestcontainersInitializer.class)
public class ExchangeRateControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private ExchangeRateClient exchangeRateClient;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    public void testNoCurrencyProvided() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(ExchangeRateController.PATH)
                .then()
                .statusCode(400);
    }

    @Test
    void testGetRates() {
        String currencyCode = "USD";
        var usdCurrency = new Currency(currencyCode, "United State Dollar");
        usdCurrency = currencyRepository.save(usdCurrency);

        var date = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Z"));
        var rateDto = new CurrencyRateDto(date, currencyCode, Map.of("EUR", new BigDecimal("1.2"), "AED", new BigDecimal("4.1")));
        Mockito.when(exchangeRateClient.getRates(Mockito.eq(currencyCode))).thenReturn(rateDto);

        exchangeRateService.updateRates(currencyCode);

        given()
                .contentType(ContentType.JSON)
                .queryParam("currency", "USD")
                .when()
                .get(ExchangeRateController.PATH)
                .then()
                .statusCode(200)
                .body(".", hasSize(1));
        exchangeRateRepository.deleteAll();
        currencyRepository.delete(usdCurrency);
    }
}
