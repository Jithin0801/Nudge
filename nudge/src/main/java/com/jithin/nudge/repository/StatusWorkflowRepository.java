package com.jithin.nudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jithin.nudge.entity.JobApplicationStatus;
import com.jithin.nudge.entity.StatusWorkflow;

@Repository
public interface StatusWorkflowRepository extends JpaRepository<StatusWorkflow, Long> {
    boolean existsByFromStatusAndToStatus(JobApplicationStatus fromStatus, JobApplicationStatus toStatus);
}
