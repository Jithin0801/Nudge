package com.jithin.nudge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jithin.nudge.dto.JobApplicationResponseDTO;
import com.jithin.nudge.entity.JobApplication;
import com.jithin.nudge.entity.JobApplicationStatus;
import com.jithin.nudge.entity.StatusWorkflow;
import com.jithin.nudge.entity.UserSecurity;
import com.jithin.nudge.repository.JobApplicationRepository;
import com.jithin.nudge.repository.JobApplicationStatusRepository;
import com.jithin.nudge.repository.StatusWorkflowRepository;
import com.jithin.nudge.repository.UserSecurityRepository;

class JobApplicationServiceTest {

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @Mock
    private UserSecurityRepository userSecurityRepository;

    @Mock
    private JobApplicationStatusRepository jobApplicationStatusRepository;

    @Mock
    private StatusWorkflowRepository statusWorkflowRepository;

    @InjectMocks
    private JobApplicationService jobApplicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetJobApplicationWithPossibleStatuses() {
        // Arrange
        String applicationId = "test-app-id";
        UserSecurity user = new UserSecurity();
        user.setEmailId("test@example.com");

        JobApplicationStatus currentStatus = JobApplicationStatus.builder().statusName("SUBMITTED").build();
        JobApplicationStatus nextStatus1 = JobApplicationStatus.builder().statusName("INTERVIEW").build();
        JobApplicationStatus nextStatus2 = JobApplicationStatus.builder().statusName("REJECTED").build();

        JobApplication jobApplication =
                JobApplication.builder().applicationId(applicationId).title("Software Engineer").status(currentStatus).user(user).active(true).lastUpdated(LocalDateTime.now()).build();

        StatusWorkflow workflowPv1 = StatusWorkflow.builder().fromStatus(currentStatus).toStatus(nextStatus1).build();
        StatusWorkflow workflowPv2 = StatusWorkflow.builder().fromStatus(currentStatus).toStatus(nextStatus2).build();

        when(jobApplicationRepository.findByApplicationIdAndActiveTrue(applicationId)).thenReturn(Optional.of(jobApplication));
        when(statusWorkflowRepository.findByFromStatus(currentStatus)).thenReturn(Arrays.asList(workflowPv1, workflowPv2));

        // Act
        JobApplicationResponseDTO result = jobApplicationService.getJobApplication(applicationId);

        // Assert
        assertNotNull(result);
        assertEquals("SUBMITTED", result.getStatus());
        assertNotNull(result.getPossibleStatuses());
        assertEquals(2, result.getPossibleStatuses().size());
        assertEquals(Arrays.asList("INTERVIEW", "REJECTED"), result.getPossibleStatuses());
        assertNotNull(result.getLastUpdated());
    }

    @Test
    void testGetAllJobApplicationsWithPossibleStatuses() {
        // Arrange
        String email = "test@example.com";
        UserSecurity user = new UserSecurity();
        user.setEmailId(email);

        JobApplicationStatus status = JobApplicationStatus.builder().statusName("SUBMITTED").build();
        JobApplication jobApp = JobApplication.builder().applicationId("app-1").status(status).user(user).active(true).build();

        StatusWorkflow workflow = StatusWorkflow.builder().fromStatus(status).toStatus(JobApplicationStatus.builder().statusName("NEXT").build()).build();

        when(userSecurityRepository.findUserByEmailId(email)).thenReturn(user);
        when(jobApplicationRepository.findByUserAndActiveTrue(user)).thenReturn(Collections.singletonList(jobApp));
        when(statusWorkflowRepository.findByFromStatus(status)).thenReturn(Collections.singletonList(workflow));

        // Act
        List<JobApplicationResponseDTO> results = jobApplicationService.getAllJobApplications(email);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("SUBMITTED", results.get(0).getStatus());
        assertNotNull(results.get(0).getPossibleStatuses());
        assertEquals(1, results.get(0).getPossibleStatuses().size());
        assertEquals("NEXT", results.get(0).getPossibleStatuses().get(0));
    }
}
