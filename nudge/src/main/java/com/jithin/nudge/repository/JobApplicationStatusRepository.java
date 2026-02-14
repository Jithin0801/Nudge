package com.jithin.nudge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jithin.nudge.entity.JobApplicationStatus;

@Repository
public interface JobApplicationStatusRepository extends JpaRepository<JobApplicationStatus, Long> {
    Optional<JobApplicationStatus> findByStatusName(String statusName);
}
