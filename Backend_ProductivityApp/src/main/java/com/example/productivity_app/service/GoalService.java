package com.example.productivity_app.service;

import com.example.productivity_app.entity.Goal;
import com.example.productivity_app.entity.Users;
import com.example.productivity_app.repository.GoalRepository;
import com.example.productivity_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GoalService {
    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    public GoalService(GoalRepository goalRepository, UserRepository userRepository) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
    }

    public Goal createGoal(Long userId, Goal goal) {
        Users user = userRepository.findById(userId).orElseThrow();
        goal.setUser(user);
        return goalRepository.save(goal);
    }

    public List<Goal> getUserGoals(Long userId) {
        return goalRepository.findByUsersId(userId);
    }

}
