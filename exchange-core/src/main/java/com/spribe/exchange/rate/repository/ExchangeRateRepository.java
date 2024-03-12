package com.spribe.exchange.rate.repository;

import com.spribe.exchange.rate.entity.ExchangeRate;
import com.spribe.exchange.rate.entity.ExchangeRateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, ExchangeRateId> {

    List<ExchangeRate> findAllByIdBase(String base);

    @Query(value = """
                   select distinct on (base) *
                   from exchange_rate
                   order by base, date desc
                   """, nativeQuery = true)
    List<ExchangeRate> getLatestRates();
}
