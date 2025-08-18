package com.example.productivity_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Checkpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "goal_id")
    private Goal goal;

    private String title;
    private String description;
    private LocalDate dueDate;
    private String status = "PENDING"; // PENDING, IN_PROGRESS, COMPLETED, OVERDUE
    private LocalDate completedDate;
}
