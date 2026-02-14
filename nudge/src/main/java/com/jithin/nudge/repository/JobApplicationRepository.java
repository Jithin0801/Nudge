package com.jithin.nudge.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jithin.nudge.entity.JobApplication;
import com.jithin.nudge.entity.UserSecurity;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByUserAndActiveTrue(UserSecurity user);

    Optional<JobApplication> findByApplicationIdAndActiveTrue(String applicationId);
}
