package com.medical.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO sent from the gateway to users-service for credential validation.
 *
 * @author Henry Fernando Mulato Llanten <henrymulato@unicauca.edu.co>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthValidationRequest {

    private String username;
    private String password;
}
