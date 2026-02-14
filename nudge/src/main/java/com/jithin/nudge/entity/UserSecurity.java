package com.jithin.nudge.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "user_security")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSecurity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false, updatable = false)
    private String userId;

    @Column(name = "email_id", unique = true, nullable = false)
    private String emailId;

    @Column(nullable = false)
    private String password;

    private boolean enabled;

    @OneToOne(mappedBy = "userSecurity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserPersonal userPersonal;

    @PrePersist
    public void generateUserId() {
        if (this.userId == null) {
            this.userId = UUID.randomUUID().toString();
        }
        if (this.enabled == false) {
            this.enabled = true;
        }
    }
}
