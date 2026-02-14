package com.jithin.nudge.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jithin.nudge.entity.JobApplicationStatus;
import com.jithin.nudge.entity.StatusWorkflow;
import com.jithin.nudge.repository.JobApplicationStatusRepository;
import com.jithin.nudge.repository.StatusWorkflowRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(JobApplicationStatusRepository statusRepository, StatusWorkflowRepository workflowRepository) {
        return args -> {
            if (statusRepository.count() == 0) {
                // Define Statuses
                List<String> statusNames = Arrays.asList("SUBMITTED", "UNDER_REVIEW", "INTERVIEW_SCHEDULED", "INTERVIEWING", "OFFER_RECEIVED", "ACCEPTED", "REJECTED", "WITHDRAWN");

                Map<String, JobApplicationStatus> statusMap = new HashMap<>();

                for (String name : statusNames) {
                    JobApplicationStatus status = JobApplicationStatus.builder().statusName(name).description("Status: " + name).build();
                    statusMap.put(name, statusRepository.save(status));
                }

                // Define Workflows (From -> List of To)
                Map<String, List<String>> workflows = new HashMap<>();

                workflows.put("SUBMITTED", Arrays.asList("UNDER_REVIEW", "REJECTED", "WITHDRAWN"));
                workflows.put("UNDER_REVIEW", Arrays.asList("INTERVIEW_SCHEDULED", "REJECTED", "WITHDRAWN"));
                workflows.put("INTERVIEW_SCHEDULED", Arrays.asList("INTERVIEWING", "WITHDRAWN"));
                workflows.put("INTERVIEWING", Arrays.asList("OFFER_RECEIVED", "REJECTED", "WITHDRAWN"));
                workflows.put("OFFER_RECEIVED", Arrays.asList("ACCEPTED", "REJECTED", "WITHDRAWN"));

                // Allow WITHDRAWN from anywhere? - following the specific list for now.
                // Re-reading logic: SUBMITTED -> withdrawn is there.

                for (Map.Entry<String, List<String>> entry : workflows.entrySet()) {
                    JobApplicationStatus from = statusMap.get(entry.getKey());
                    if (from == null)
                        continue;

                    for (String toName : entry.getValue()) {
                        JobApplicationStatus to = statusMap.get(toName);
                        if (to != null) {
                            StatusWorkflow workflow = StatusWorkflow.builder().fromStatus(from).toStatus(to).build();
                            workflowRepository.save(workflow);
                        }
                    }
                }

                System.out.println("Initialized Job Application Statuses and Workflows");
            }
        };
    }
}
