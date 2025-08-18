package com.example.productivity_app.controller;

import com.example.productivity_app.dto.BuddyRequestDto;
import com.example.productivity_app.entity.BuddyRequest;
import com.example.productivity_app.mapper.BuddyRequestMapper;
import com.example.productivity_app.service.BuddyRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/buddies")
public class BuddyRequestController {
    private final BuddyRequestService buddyRequestService;
    private final BuddyRequestMapper buddyRequestMapper;

    public BuddyRequestController(BuddyRequestService buddyRequestService, BuddyRequestMapper buddyRequestMapper) {
        this.buddyRequestService = buddyRequestService;
        this.buddyRequestMapper = buddyRequestMapper;
    }

    @PostMapping("/send")
    public ResponseEntity<BuddyRequestDto> sendBuddyRequest(@RequestBody BuddyRequestDto requestDto) {
        BuddyRequest request = buddyRequestService.sendRequest(requestDto.getSenderId(), requestDto.getReceiverId());
        return ResponseEntity.ok(buddyRequestMapper.toDto(request));
    }

    @PostMapping("/{requestId}/accept")
    public ResponseEntity<BuddyRequestDto> acceptBuddyRequest(@PathVariable Long requestId, @RequestParam Long userId) {
        BuddyRequest request = buddyRequestService.acceptRequest(requestId, userId);
        return ResponseEntity.ok(buddyRequestMapper.toDto(request));
    }

    @PostMapping("/{requestId}/reject")
    public ResponseEntity<BuddyRequestDto> rejectBuddyRequest(@PathVariable Long requestId, @RequestParam Long userId) {
        BuddyRequest request = buddyRequestService.rejectRequest(requestId, userId);
        return ResponseEntity.ok(buddyRequestMapper.toDto(request));
    }

    @GetMapping("/pending/{userId}")
    public ResponseEntity<List<BuddyRequestDto>> getPendingRequests(@PathVariable Long userId) {
        List<BuddyRequest> requests = buddyRequestService.getPendingRequestsForUser(userId);
        List<BuddyRequestDto> dtos = requests.stream()
                .map(buddyRequestMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/sent/{userId}")
    public ResponseEntity<List<BuddyRequestDto>> getSentRequests(@PathVariable Long userId) {
        List<BuddyRequest> requests = buddyRequestService.getSentRequestsForUser(userId);
        List<BuddyRequestDto> dtos = requests.stream()
                .map(buddyRequestMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/accepted/{userId}")
    public ResponseEntity<List<BuddyRequestDto>> getAcceptedBuddies(@PathVariable Long userId) {
        List<BuddyRequest> requests = buddyRequestService.getAcceptedBuddiesForUser(userId);
        List<BuddyRequestDto> dtos = requests.stream()
                .map(buddyRequestMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}