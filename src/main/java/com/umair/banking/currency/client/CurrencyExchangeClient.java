package com.umair.banking.currency.client;


import com.umair.banking.account.enums.Currency;
import com.umair.banking.currency.dto.ExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class CurrencyExchangeClient {

    private final RestClient currencyExchangeRestClient;

    public ExchangeRateResponse getExchangeRate(Currency from, Currency to) {

        return currencyExchangeRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/rate/{from}/{to}")
                        .build(from.name(),to.name()))
                .retrieve()
                .body(ExchangeRateResponse.class);
    }


}
