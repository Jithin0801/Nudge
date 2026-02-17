package com.jithin.nudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jithin.nudge.entity.JobApplicationResume;

@Repository
public interface JobApplicationResumeRepository extends JpaRepository<JobApplicationResume, Long> {
}
