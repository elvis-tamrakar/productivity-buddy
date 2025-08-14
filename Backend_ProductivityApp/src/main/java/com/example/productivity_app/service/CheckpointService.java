package com.example.productivity_app.service;

import com.example.productivity_app.entity.Checkpoint;
import com.example.productivity_app.entity.Goal;
import com.example.productivity_app.repository.CheckpointRepository;
import com.example.productivity_app.repository.GoalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
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

        // Set the goal for the checkpoint
        checkpoint.setGoal(goal);
        
        // Save the checkpoint first
        Checkpoint savedCheckpoint = checkpointRepository.save(checkpoint);
        
        // Add to goal's checkpoint list and update progress
        goal.getCheckpoints().add(savedCheckpoint);
        goal.calculateProgress();
        goalRepository.save(goal);
        
        return savedCheckpoint;
    }

    public List<Checkpoint> getCheckpointsForGoal(Long goalId) {
        return checkpointRepository.findByGoal_Id(goalId);
    }

    public Checkpoint updateCheckpoint(Long checkpointId, Checkpoint checkpointDetails) {
        Checkpoint checkpoint = checkpointRepository.findById(checkpointId)
                .orElseThrow(() -> new RuntimeException("Checkpoint not found"));

        if (checkpointDetails.getTitle() != null) {
            checkpoint.setTitle(checkpointDetails.getTitle());
        }
        if (checkpointDetails.getDescription() != null) {
            checkpoint.setDescription(checkpointDetails.getDescription());
        }
        if (checkpointDetails.getDueDate() != null) {
            checkpoint.setDueDate(checkpointDetails.getDueDate());
        }
        if (checkpointDetails.getStatus() != null) {
            checkpoint.setStatus(checkpointDetails.getStatus());
        }

        Checkpoint updatedCheckpoint = checkpointRepository.save(checkpoint);
        
        // Update goal progress
        Goal goal = checkpoint.getGoal();
        goal.calculateProgress();
        goalRepository.save(goal);
        
        return updatedCheckpoint;
    }

    public Checkpoint completeCheckpoint(Long checkpointId) {
        Checkpoint checkpoint = checkpointRepository.findById(checkpointId)
                .orElseThrow(() -> new RuntimeException("Checkpoint not found"));

        checkpoint.setStatus("COMPLETED");
        checkpoint.setCompletedDate(LocalDate.now());
        
        Checkpoint completedCheckpoint = checkpointRepository.save(checkpoint);
        
        // Update goal progress
        Goal goal = checkpoint.getGoal();
        goal.calculateProgress();
        goalRepository.save(goal);
        
        return completedCheckpoint;
    }

    public void deleteCheckpoint(Long checkpointId) {
        Checkpoint checkpoint = checkpointRepository.findById(checkpointId)
                .orElseThrow(() -> new RuntimeException("Checkpoint not found"));
        
        Goal goal = checkpoint.getGoal();
        
        // Remove from goal's checkpoint list
        goal.getCheckpoints().remove(checkpoint);
        
        // Delete the checkpoint
        checkpointRepository.delete(checkpoint);
        
        // Update goal progress
        goal.calculateProgress();
        goalRepository.save(goal);
    }
}
