package com.medical;

import com.medical.config.JwtConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * API Gateway entry point for Piedrazul Medical Services Network.
 * Routes and secures all microservice endpoints.
 *
 * @author Henry Fernando Mulato Llanten <henrymulato@unicauca.edu.co>
 */
@SpringBootApplication
@EnableConfigurationProperties(JwtConfig.class)
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
