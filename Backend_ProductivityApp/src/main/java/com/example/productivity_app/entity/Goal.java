package com.example.productivity_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status = "ACTIVE"; // ACTIVE, COMPLETED, PAUSED, CANCELLED
    private Integer progress = 0; // 0-100 percentage

    @OneToMany(mappedBy = "goal", fetch = FetchType.LAZY)
    private List<Checkpoint> checkpoints = new ArrayList<>();

    public void setUser(Users users) {
        this.users = users;
    }

    public void calculateProgress() {
        if (checkpoints.isEmpty()) {
            this.progress = 0;
            return;
        }
        
        long completedCheckpoints = checkpoints.stream()
                .filter(checkpoint -> "COMPLETED".equals(checkpoint.getStatus()))
                .count();
        
        this.progress = (int) ((completedCheckpoints * 100) / checkpoints.size());
    }
}
