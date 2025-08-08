package com.example.productivity_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String startDate;
    private String endDate;

    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL)
    private List<Checkpoint> checkpoints = new ArrayList<>();

    public void setUser(Users users) {
        this.users = users;
    }
}
