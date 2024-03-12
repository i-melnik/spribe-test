package com.spribe.exchange.currency.controller;

import com.spribe.exchange.TestConfiguration;
import com.spribe.exchange.TestcontainersInitializer;
import com.spribe.exchange.currency.dto.CurrencyDto;
import com.spribe.exchange.currency.entity.Currency;
import com.spribe.exchange.currency.repository.CurrencyRepository;
import com.spribe.exchange.currency.service.CurrencyService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfiguration.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@ContextConfiguration(initializers = TestcontainersInitializer.class)
public class CurrencyControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    CurrencyRepository currencyRepository;

    @Autowired
    CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    void testGetAllCurrencies() {
        var allCurrency = new Currency("ALL", "All desc");
        var usdCurrency = new Currency("USD", "United State Dollar");
        List<Currency> currencies = List.of(allCurrency, usdCurrency);
        currencies = currencyRepository.saveAll(currencies);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get(CurrencyController.PATH)
                .then()
                .statusCode(200)
                .body(".", hasSize(2));
        currencyRepository.deleteAll(currencies);
    }

    @Test
    void testAddCurrency() {
        var dto = new CurrencyDto("USD", null);

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post(CurrencyController.PATH)
                .then()
                .statusCode(200);
        currencyRepository.deleteAll();
    }

    @Test
    void testAddCurrencyWithoutCode() {
        var dto = new CurrencyDto(null, "Desc");

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post(CurrencyController.PATH)
                .then()
                .statusCode(400);
    }
}
