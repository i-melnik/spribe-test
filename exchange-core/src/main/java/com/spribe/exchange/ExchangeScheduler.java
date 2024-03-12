package com.spribe.exchange;

import com.spribe.exchange.currency.repository.CurrencyRepository;
import com.spribe.exchange.rate.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExchangeScheduler {

    private final ExchangeRateService exchangeRateService;
    private final CurrencyRepository currencyRepository;
    private final ExecutorService executorService;

    @Scheduled(cron = "${exchange.rate.scheduler.cron:0 1 * * * *}")
    public void updateRates() {
        log.info("Exchange rate scheduler. Starting update.");
        var currencies = currencyRepository.findAll();
        List<CompletableFuture<Void>> tasks = new ArrayList<>();
        for (var currency : currencies) {
            tasks.add(CompletableFuture.runAsync(() -> {
                try {
                    exchangeRateService.updateRates(currency.getCode());
                } catch (Exception e) {
                    log.error("Failed to get rates for currency %s".formatted(currency.getCode()), e);
                }
            }, executorService));

        }
        var ignored = tasks.stream().map(CompletableFuture::join).toList();
        log.info("Exchange rate scheduler. Finished update.");
    }
}
