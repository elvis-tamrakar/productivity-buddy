package com.example.productivity_app.controller;

import com.example.productivity_app.dto.GoalDto;
import com.example.productivity_app.entity.Goal;
import com.example.productivity_app.mapper.GoalMapper;
import com.example.productivity_app.service.GoalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/goals")
public class GoalController {
    private final GoalService goalService;
    private final GoalMapper goalMapper;

    public GoalController(GoalService goalService, GoalMapper goalMapper) {
        this.goalService = goalService;
        this.goalMapper = goalMapper;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<GoalDto> createGoal(@PathVariable Long userId, @RequestBody Goal goal) {
        Goal created = goalService.createGoal(userId, goal);
        return ResponseEntity.ok(goalMapper.toDto(created));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GoalDto>> getUserGoals(@PathVariable Long userId) {
        return ResponseEntity.ok(goalService.getUserGoals(userId).stream()
                .map(goalMapper::toDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{goalId}")
    public ResponseEntity<GoalDto> getGoalById(@PathVariable Long goalId) {
        Goal goal = goalService.getGoalById(goalId);
        return ResponseEntity.ok(goalMapper.toDto(goal));
    }

    @PutMapping("/{goalId}")
    public ResponseEntity<GoalDto> updateGoal(@PathVariable Long goalId, @RequestBody Goal goal) {
        Goal updated = goalService.updateGoal(goalId, goal);
        return ResponseEntity.ok(goalMapper.toDto(updated));
    }

    @DeleteMapping("/{goalId}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long goalId) {
        goalService.deleteGoal(goalId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{goalId}/complete")
    public ResponseEntity<GoalDto> completeGoal(@PathVariable Long goalId) {
        Goal completed = goalService.completeGoal(goalId);
        return ResponseEntity.ok(goalMapper.toDto(completed));
    }
}