package com.example.productivity_app.controller;

import com.example.productivity_app.dto.CheckpointDto;
import com.example.productivity_app.entity.Checkpoint;
import com.example.productivity_app.mapper.CheckpointMapper;
import com.example.productivity_app.service.CheckpointService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/checkpoints")
public class CheckpointController {
    private final CheckpointService checkpointService;
    private final CheckpointMapper checkpointMapper;

    public CheckpointController(CheckpointService checkpointService, CheckpointMapper checkpointMapper) {
        this.checkpointService = checkpointService;
        this.checkpointMapper = checkpointMapper;
    }

    @PostMapping("/{goalId}")
    public ResponseEntity<CheckpointDto> addCheckpoint(@PathVariable Long goalId, @RequestBody Checkpoint checkpoint) {
        Checkpoint created = checkpointService.addCheckpointToGoal(goalId, checkpoint);
        return ResponseEntity.ok(checkpointMapper.toDto(created));
    }

    @GetMapping("/goal/{goalId}")
    public ResponseEntity<List<CheckpointDto>> getCheckpointsForGoal(@PathVariable Long goalId) {
        List<Checkpoint> checkpoints = checkpointService.getCheckpointsForGoal(goalId);
        List<CheckpointDto> dtos = checkpoints.stream()
                .map(checkpointMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{checkpointId}")
    public ResponseEntity<CheckpointDto> updateCheckpoint(@PathVariable Long checkpointId, @RequestBody Checkpoint checkpoint) {
        Checkpoint updated = checkpointService.updateCheckpoint(checkpointId, checkpoint);
        return ResponseEntity.ok(checkpointMapper.toDto(updated));
    }

    @PostMapping("/{checkpointId}/complete")
    public ResponseEntity<CheckpointDto> completeCheckpoint(@PathVariable Long checkpointId) {
        Checkpoint completed = checkpointService.completeCheckpoint(checkpointId);
        return ResponseEntity.ok(checkpointMapper.toDto(completed));
    }

    @DeleteMapping("/{checkpointId}")
    public ResponseEntity<Void> deleteCheckpoint(@PathVariable Long checkpointId) {
        checkpointService.deleteCheckpoint(checkpointId);
        return ResponseEntity.noContent().build();
    }
}