package com.medical.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for JWT token generation and validation.
 * Bound to the "jwt" prefix in application.yml.
 *
 * @author Henry Fernando Mulato Llanten <henrymulato@unicauca.edu.co>
 */
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private String secret;
    private long expiration;
}
