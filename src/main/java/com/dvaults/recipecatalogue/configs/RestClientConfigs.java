package com.dvaults.recipecatalogue.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class RestClientConfigs {

  @Bean
  public RestClient restClient() {

    JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(
        HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(2))
            .build()
    );
    requestFactory.setReadTimeout(Duration.ofSeconds(5));

    return RestClient.builder()
        .requestFactory(requestFactory)
        .build();

  }

}
