package com.jithin.nudge.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;

    private String summary;

    @NotBlank(message = "Company name is required")
    private String companyName;

    private LocalDate appliedDate;

    private String applicationType;



    private byte[] resume;
}
