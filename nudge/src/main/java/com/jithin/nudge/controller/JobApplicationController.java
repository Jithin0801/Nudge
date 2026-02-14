package com.jithin.nudge.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jithin.nudge.dto.JobApplicationRequestDTO;
import com.jithin.nudge.dto.JobApplicationResponseDTO;
import com.jithin.nudge.service.JobApplicationService;
import com.jithin.nudge.util.ResponseWrapper;
import com.jithin.nudge.util.WrappedResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/job-applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new org.springframework.security.authentication.AuthenticationCredentialsNotFoundException("User not authenticated");
        }
        if (authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return authentication.getName();
    }

    @PostMapping
    public ResponseEntity<WrappedResponse<String>> addJobApplication(@RequestBody @Valid JobApplicationRequestDTO jobApplicationDTO) {
        jobApplicationService.addJobApplication(jobApplicationDTO, getAuthenticatedUserEmail());
        return ResponseWrapper.created("Job Application added successfully", null);
    }

    @GetMapping
    public ResponseEntity<WrappedResponse<List<JobApplicationResponseDTO>>> getAllJobApplications() {
        List<JobApplicationResponseDTO> jobApplications = jobApplicationService.getAllJobApplications(getAuthenticatedUserEmail());
        return ResponseWrapper.ok("Fetched all job applications", jobApplications);
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<WrappedResponse<JobApplicationResponseDTO>> getJobApplication(@PathVariable String applicationId) {
        JobApplicationResponseDTO jobApplication = jobApplicationService.getJobApplication(applicationId);
        return ResponseWrapper.ok("Fetched job application", jobApplication);
    }

    @org.springframework.web.bind.annotation.PatchMapping("/{applicationId}/status")
    public ResponseEntity<WrappedResponse<String>> updateStatus(@PathVariable String applicationId, @org.springframework.web.bind.annotation.RequestParam String status) {
        jobApplicationService.updateJobApplicationStatus(applicationId, status);
        return ResponseWrapper.ok("Status updated successfully", null);
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<WrappedResponse<String>> deleteJobApplication(@PathVariable String applicationId) {
        jobApplicationService.deleteJobApplication(applicationId);
        return ResponseWrapper.ok("Job Application deleted successfully", null);
    }
}
