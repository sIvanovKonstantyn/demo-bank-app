package com.home.demos.deposit.endtoend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Objects;

public class DepositEndToEndTest {
    @Test
    void createDeposit_whenAllComponentsWorkRight_thenSuccessfulCodeShouldBePresent() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpRequest = new HttpEntity<>(
                "{\n" +
                        "\"name\" : \"test-dep-name\",\n" +
                        "\"sum\" : 100,\n" +
                        "\"capitalizationType\" : \"type\",\n" +
                        "\"currencyCode\" : 840,\n" +
                        "\"depositType\" : \"type\",\n" +
                        "\"closeDate\" : \"2022-01-01T01:01:01\",\n" +
                        "\"incomeRate\" : 5\n" +
                        "}",
                headers
        );


        String result = restTemplate.postForObject(
                "http://localhost:8083/deposit/create",
                httpRequest,
                String.class
        );

        Assertions.assertTrue(Objects.requireNonNull(result).contains("\"resultCode\":0"));
    }
}
