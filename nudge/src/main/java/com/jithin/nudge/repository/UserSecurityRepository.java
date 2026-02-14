package com.jithin.nudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jithin.nudge.entity.UserSecurity;

@Repository
public interface UserSecurityRepository extends JpaRepository<UserSecurity, Long> {
    UserSecurity findUserByEmailId(String emailId);
}
