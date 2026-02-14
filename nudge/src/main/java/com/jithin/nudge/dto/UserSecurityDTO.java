package com.jithin.nudge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSecurityDTO {

    @NotBlank(message = "Login ID is required")
    @Size(min = 3, max = 50, message = "Login ID must be between 3 and 50 characters")
    private String loginId;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}
