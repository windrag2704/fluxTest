package com.example.fluxtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.fluxtest.client.FluxClient;

@SpringBootApplication
public class FluxTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(FluxTestApplication.class, args);
        FluxClient client = new FluxClient();
        client.process();
    }

}
