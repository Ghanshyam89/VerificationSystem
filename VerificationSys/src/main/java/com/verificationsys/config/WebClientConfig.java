package com.verificationsys.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    @Bean(name = "randomUserWebClient")
    WebClient api1WebClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().responseTimeout(java.time.Duration.ofMillis(2000))))
                .build();
    }

    @Bean(name = "nationalizeWebClient")
    WebClient api2WebClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().responseTimeout(java.time.Duration.ofMillis(1000))))
                .build();
    }

    @Bean(name = "genderizeWebClient")
    WebClient api3WebClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().responseTimeout(java.time.Duration.ofMillis(1000))))
                .build();
    }
}
