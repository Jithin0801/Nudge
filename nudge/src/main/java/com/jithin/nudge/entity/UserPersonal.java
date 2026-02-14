package com.jithin.nudge.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "user_personal")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPersonal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_security_id", referencedColumnName = "id", nullable = false)
    private UserSecurity userSecurity;

    @PrePersist
    public void syncUserId() {
        if (userSecurity != null) {
            this.userId = userSecurity.getUserId();
        }
    }
}
