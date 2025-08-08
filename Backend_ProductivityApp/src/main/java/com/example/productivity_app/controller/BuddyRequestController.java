package com.example.productivity_app.controller;

import com.example.productivity_app.dto.BuddyRequestDto;
import com.example.productivity_app.entity.BuddyRequest;
import com.example.productivity_app.mapper.BuddyRequestMapper;
import com.example.productivity_app.service.BuddyRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/buddies")
public class BuddyRequestController {
    private final BuddyRequestService buddyRequestService;
    private final BuddyRequestMapper buddyRequestMapper;

    public BuddyRequestController(BuddyRequestService buddyRequestService, BuddyRequestMapper buddyRequestMapper) {
        this.buddyRequestService = buddyRequestService;
        this.buddyRequestMapper = buddyRequestMapper;
    }

}