package com.medical.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for successful login — returned when credentials are valid.
 * @author Henry Fernando Mulato Llanten <henrymulato@unicauca.edu.co>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

  private Long userId;
  private String username;
  private String role;
}
