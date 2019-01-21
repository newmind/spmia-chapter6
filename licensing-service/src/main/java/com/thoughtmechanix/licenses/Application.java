package com.thoughtmechanix.licenses;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RefreshScope
@EnableDiscoveryClient
@EnableFeignClients
public class Application {

  // 리븐을 지원하는 RestTemplate
  @LoadBalanced // [필수] RestTemplate 에서 리븐을 지원하려면,
  @Bean
  public RestTemplate getRestTemplate(){
      return new RestTemplate();
  }
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}