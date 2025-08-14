package com.example.productivity_app.service;

import com.example.productivity_app.entity.Goal;
import com.example.productivity_app.entity.Users;
import com.example.productivity_app.repository.GoalRepository;
import com.example.productivity_app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public Goal getGoalById(Long goalId) {
        return goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));
    }

    public Goal updateGoal(Long goalId, Goal goalDetails) {
        Goal goal = getGoalById(goalId);
        
        if (goalDetails.getTitle() != null) {
            goal.setTitle(goalDetails.getTitle());
        }
        if (goalDetails.getDescription() != null) {
            goal.setDescription(goalDetails.getDescription());
        }
        if (goalDetails.getStartDate() != null) {
            goal.setStartDate(goalDetails.getStartDate());
        }
        if (goalDetails.getEndDate() != null) {
            goal.setEndDate(goalDetails.getEndDate());
        }
        if (goalDetails.getStatus() != null) {
            goal.setStatus(goalDetails.getStatus());
        }
        
        return goalRepository.save(goal);
    }

    public void deleteGoal(Long goalId) {
        Goal goal = getGoalById(goalId);
        goalRepository.delete(goal);
    }

    public Goal completeGoal(Long goalId) {
        Goal goal = getGoalById(goalId);
        goal.setStatus("COMPLETED");
        goal.setProgress(100);
        return goalRepository.save(goal);
    }
}
