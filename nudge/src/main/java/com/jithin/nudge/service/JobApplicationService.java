package com.jithin.nudge.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jithin.nudge.dto.JobApplicationRequestDTO;
import com.jithin.nudge.dto.JobApplicationResponseDTO;
import com.jithin.nudge.entity.JobApplication;
import com.jithin.nudge.entity.JobApplicationResume;
import com.jithin.nudge.entity.JobApplicationStatus;
import com.jithin.nudge.repository.JobApplicationStatusRepository;
import com.jithin.nudge.repository.StatusWorkflowRepository;
import com.jithin.nudge.entity.UserSecurity;
import com.jithin.nudge.repository.JobApplicationRepository;
import com.jithin.nudge.repository.UserSecurityRepository;

@Service
@Transactional
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final UserSecurityRepository userSecurityRepository;
    private final JobApplicationStatusRepository jobApplicationStatusRepository;
    private final StatusWorkflowRepository statusWorkflowRepository;

    public JobApplicationService(JobApplicationRepository jobApplicationRepository, UserSecurityRepository userSecurityRepository,
            JobApplicationStatusRepository jobApplicationStatusRepository, StatusWorkflowRepository statusWorkflowRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.userSecurityRepository = userSecurityRepository;
        this.jobApplicationStatusRepository = jobApplicationStatusRepository;
        this.statusWorkflowRepository = statusWorkflowRepository;
    }

    public JobApplicationResponseDTO addJobApplication(JobApplicationRequestDTO jobApplicationDTO, String email) {
        UserSecurity user = userSecurityRepository.findUserByEmailId(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        JobApplicationStatus defaultStatus = jobApplicationStatusRepository.findByStatusName("SUBMITTED").orElseThrow(() -> new RuntimeException("Default status 'SUBMITTED' not found"));

        JobApplication jobApplication = JobApplication.builder().title(jobApplicationDTO.getTitle()).summary(jobApplicationDTO.getSummary()).companyName(jobApplicationDTO.getCompanyName())
                .appliedDate(jobApplicationDTO.getAppliedDate()).nextFollowUpDate(jobApplicationDTO.getAppliedDate().plusDays(7)).applicationType(jobApplicationDTO.getApplicationType())
                .status(defaultStatus).user(user).active(true).build();

        if (jobApplicationDTO.getResume() != null) {
            JobApplicationResume resume =
                    JobApplicationResume.builder().fileName(jobApplicationDTO.getResumeFilename()).data(jobApplicationDTO.getResume()).jobApplication(jobApplication).build();
            jobApplication.setJobApplicationResume(resume);
            jobApplication.setResumeFilename(jobApplicationDTO.getResumeFilename());
        }

        return mapToDTO(jobApplicationRepository.save(jobApplication));
    }

    public List<JobApplicationResponseDTO> getAllJobApplications(String email) {
        UserSecurity user = userSecurityRepository.findUserByEmailId(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return jobApplicationRepository.findByUserAndActiveTrue(user).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public JobApplicationResponseDTO getJobApplication(String applicationId) {
        JobApplication jobApplication = jobApplicationRepository.findByApplicationIdAndActiveTrue(applicationId).orElseThrow(() -> new RuntimeException("Job Application not found"));
        return mapToDTO(jobApplication);
    }

    public void deleteJobApplication(String applicationId) {
        JobApplication jobApplication = jobApplicationRepository.findByApplicationIdAndActiveTrue(applicationId).orElseThrow(() -> new RuntimeException("Job Application not found"));
        jobApplication.setActive(false);
        jobApplicationRepository.save(jobApplication);
    }

    public void updateJobApplicationStatus(String applicationId, String newStatusName) {
        JobApplication jobApplication = jobApplicationRepository.findByApplicationIdAndActiveTrue(applicationId).orElseThrow(() -> new RuntimeException("Job Application not found"));

        JobApplicationStatus newStatus = jobApplicationStatusRepository.findByStatusName(newStatusName).orElseThrow(() -> new RuntimeException("Status '" + newStatusName + "' not found"));

        if (!statusWorkflowRepository.existsByFromStatusAndToStatus(jobApplication.getStatus(), newStatus)) {
            throw new RuntimeException("Invalid status transition from " + jobApplication.getStatus().getStatusName() + " to " + newStatusName);
        }

        jobApplication.setStatus(newStatus);
        jobApplicationRepository.save(jobApplication);
    }

    public byte[] downloadResume(String applicationId) {
        JobApplication jobApplication = jobApplicationRepository.findByApplicationIdAndActiveTrue(applicationId).orElseThrow(() -> new RuntimeException("Job Application not found"));

        if (jobApplication.getJobApplicationResume() != null) {
            return jobApplication.getJobApplicationResume().getData();
        }
        return null;
    }

    public String getResumeFilename(String applicationId) {
        JobApplication jobApplication = jobApplicationRepository.findByApplicationIdAndActiveTrue(applicationId).orElseThrow(() -> new RuntimeException("Job Application not found"));
        return jobApplication.getResumeFilename();
    }

    private JobApplicationResponseDTO mapToDTO(JobApplication jobApplication) {
        List<String> possibleStatuses =
                statusWorkflowRepository.findByFromStatus(jobApplication.getStatus()).stream().map(workflow -> workflow.getToStatus().getStatusName()).collect(Collectors.toList());

        Long resumeId = jobApplication.getJobApplicationResume() != null ? jobApplication.getJobApplicationResume().getId() : null;

        return JobApplicationResponseDTO.builder().applicationId(jobApplication.getApplicationId()).title(jobApplication.getTitle()).summary(jobApplication.getSummary())
                .companyName(jobApplication.getCompanyName()).appliedDate(jobApplication.getAppliedDate()).nextFollowUpDate(jobApplication.getNextFollowUpDate())
                .applicationType(jobApplication.getApplicationType()).status(jobApplication.getStatus().getStatusName()).resumeFilename(jobApplication.getResumeFilename()).resumeId(resumeId)
                .possibleStatuses(possibleStatuses).lastUpdated(jobApplication.getLastUpdated()).build();
    }
}
