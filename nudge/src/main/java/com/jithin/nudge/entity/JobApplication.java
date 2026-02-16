package com.jithin.nudge.entity;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "job_application")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "application_id", unique = true, nullable = false, updatable = false)
    private String applicationId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "applied_date")
    private LocalDate appliedDate;

    @Column(name = "next_follow_up_date")
    private LocalDate nextFollowUpDate;

    @Column(name = "application_type")
    private String applicationType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", nullable = false)
    private JobApplicationStatus status;

    @Builder.Default
    private boolean active = true;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] resume;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_security_id", nullable = false)
    private UserSecurity user;

    @Column(name = "last_updated")
    private java.time.LocalDateTime lastUpdated;

    @PrePersist
    public void prePersist() {
        if (this.applicationId == null) {
            this.applicationId = UUID.randomUUID().toString();
        }
        if (this.appliedDate == null) {
            this.appliedDate = LocalDate.now();
        }
        this.lastUpdated = java.time.LocalDateTime.now();
    }

    @jakarta.persistence.PreUpdate
    public void preUpdate() {
        this.lastUpdated = java.time.LocalDateTime.now();
    }
}
