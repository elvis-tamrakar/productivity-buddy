package com.example.productivity_app.service;

import com.example.productivity_app.entity.BuddyRequest;
import com.example.productivity_app.entity.Users;
import com.example.productivity_app.repository.BuddyRequestRepository;
import com.example.productivity_app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BuddyRequestService {

    private final BuddyRequestRepository buddyRequestRepository;
    private final UserRepository userRepository;

    public BuddyRequestService(BuddyRequestRepository buddyRequestRepository,
                               UserRepository userRepository) {
        this.buddyRequestRepository = buddyRequestRepository;
        this.userRepository = userRepository;
    }

    public BuddyRequest sendRequest(Long senderId, Long receiverId) {
        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("Cannot send request to yourself");
        }

        // Check if request already exists
        if (buddyRequestRepository.existsByRequester_IdAndReceiver_Id(senderId, receiverId)) {
            throw new IllegalArgumentException("Buddy request already exists");
        }

        Users requester = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Requester not found"));
        Users receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        BuddyRequest request = new BuddyRequest();
        request.setRequester(requester);
        request.setReceiver(receiver);
        request.setStatus("PENDING");
        request.setDate(LocalDate.now());

        return buddyRequestRepository.save(request);
    }

    public BuddyRequest acceptRequest(Long requestId, Long receiverId) {
        BuddyRequest request = buddyRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getReceiver().getId() != receiverId) {
            throw new IllegalArgumentException("You can only accept requests sent to you");
        }

        if (!"PENDING".equals(request.getStatus())) {
            throw new IllegalArgumentException("Request is not pending");
        }

        request.setStatus("ACCEPTED");
        return buddyRequestRepository.save(request);
    }

    public BuddyRequest rejectRequest(Long requestId, Long receiverId) {
        BuddyRequest request = buddyRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getReceiver().getId() != receiverId) {
            throw new IllegalArgumentException("You can only reject requests sent to you");
        }

        if (!"PENDING".equals(request.getStatus())) {
            throw new IllegalArgumentException("Request is not pending");
        }

        request.setStatus("REJECTED");
        return buddyRequestRepository.save(request);
    }

    public List<BuddyRequest> getPendingRequestsForUser(Long userId) {
        return buddyRequestRepository.findByReceiver_IdAndStatus(userId, "PENDING");
    }

    public List<BuddyRequest> getSentRequestsForUser(Long userId) {
        return buddyRequestRepository.findByRequester_Id(userId);
    }

    public List<BuddyRequest> getAcceptedBuddiesForUser(Long userId) {
        return buddyRequestRepository.findByUserIdAndStatus(userId, "ACCEPTED");
    }
}
