package com.example.productivity_app.service;

import com.example.productivity_app.entity.Checkpoint;
import com.example.productivity_app.entity.Goal;
import com.example.productivity_app.repository.CheckpointRepository;
import com.example.productivity_app.repository.GoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckpointService {


    private final CheckpointRepository checkpointRepository;
    private final GoalRepository goalRepository;

    public CheckpointService(CheckpointRepository checkpointRepository, GoalRepository goalRepository) {
        this.checkpointRepository = checkpointRepository;
        this.goalRepository = goalRepository;
    }

    public Checkpoint addCheckpointToGoal(Long goalId, Checkpoint checkpoint) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        checkpoint.setGoal(goal);
        return checkpointRepository.save(checkpoint);
    }

}
