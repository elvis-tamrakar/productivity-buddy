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
public class BuddyRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "requesterId")
    private Users requester;

    @ManyToOne
    @JoinColumn(name = "receiverId")
    private Users receiver;

    private LocalDate date;
    private String status; // PENDING, ACCEPTED, REJECTED
}
