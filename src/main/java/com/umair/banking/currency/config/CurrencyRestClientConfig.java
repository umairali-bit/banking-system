package com.umair.banking.currency.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class CurrencyRestClientConfig {

    @Value("${currency.exchange.base-url}")
    private String baseUrl;

    @Bean
    public RestClient currencyRestClient() {

        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultStatusHandler(
                        status -> status.is5xxServerError() || status.is4xxClientError(),
                        (request, response) -> {
                            throw new RuntimeException("Currency Rest Client Error");
                        }
                )
                .build();

    }
}
