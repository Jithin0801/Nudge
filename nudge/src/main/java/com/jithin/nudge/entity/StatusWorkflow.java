package com.jithin.nudge.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "status_workflow")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusWorkflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_status_id", nullable = false)
    private JobApplicationStatus fromStatus;

    @ManyToOne
    @JoinColumn(name = "to_status_id", nullable = false)
    private JobApplicationStatus toStatus;
}
