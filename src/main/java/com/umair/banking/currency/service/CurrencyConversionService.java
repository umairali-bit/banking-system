package com.umair.banking.currency.service;


import com.umair.banking.account.enums.Currency;
import com.umair.banking.currency.client.CurrencyExchangeClient;
import com.umair.banking.currency.dto.ExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class CurrencyConversionService {

    public final CurrencyExchangeClient currencyExchangeClient;


    public BigDecimal convert(BigDecimal amount, Currency from, Currency to) {

        if (from == to) {
            return amount;
        }


//      fetch exchange rate
        ExchangeRateResponse exchangeRateResponse = currencyExchangeClient.getExchangeRate(from, to);

//        convert amount
        return amount.multiply(exchangeRateResponse.rate())
                .setScale(2, RoundingMode.HALF_UP);


    }

    public BigDecimal convertToUsd(BigDecimal amount, Currency from) {

        return convert(amount, from, Currency.USD);


    }
}
