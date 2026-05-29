package com.medical.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO received from users-service after credential validation.
 * Contains the user's ID, username, and role for JWT generation.
 *
 * @author Henry Fernando Mulato Llanten <henrymulato@unicauca.edu.co>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthValidationResponse {

    private Long userId;
    private String username;
    private String role;
}
