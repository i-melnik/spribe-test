package com.spribe.exchange.currency.repository;

import com.spribe.exchange.currency.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, String> {

    Optional<Currency> findByCode(String code);

    Currency getByCode(String code);

    boolean existsByCode(String code);
}
