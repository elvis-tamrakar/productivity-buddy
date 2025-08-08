package com.example.productivity_app.controller;

import com.example.productivity_app.dto.CheckpointDto;
import com.example.productivity_app.entity.Checkpoint;
import com.example.productivity_app.mapper.CheckpointMapper;
import com.example.productivity_app.service.CheckpointService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkpoints")
public class CheckpointController {
    private final CheckpointService checkpointService;
    private final CheckpointMapper checkpointMapper;

    public CheckpointController(CheckpointService checkpointService, CheckpointMapper checkpointMapper) {
        this.checkpointService = checkpointService;
        this.checkpointMapper = checkpointMapper;
    }


}