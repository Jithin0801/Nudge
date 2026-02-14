package com.jithin.nudge.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationResponseDTO {

    private String applicationId;
    private String title;
    private String summary;
    private String companyName;
    private LocalDate appliedDate;
    private LocalDate nextFollowUpDate;
    private String applicationType;
    private String status;
    private byte[] resume;
}
