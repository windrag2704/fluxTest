package com.example.fluxtest.client;

import org.springframework.web.reactive.function.client.WebClient;

public class FluxClient {
    WebClient client = WebClient.create("http://localhost:8080");

    public void process() {
        client.get()
                .uri("/api/call-func")
                .retrieve()
                .bodyToFlux(String.class)
                .subscribe(System.out::println);
    }
}
