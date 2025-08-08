package com.example.productivity_app.service;

import com.example.productivity_app.entity.BuddyRequest;
import com.example.productivity_app.entity.Users;
import com.example.productivity_app.repository.BuddyRequestRepository;
import com.example.productivity_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BuddyRequestService {

    private BuddyRequestRepository buddyRequestRepository;
    private UserRepository userRepository;

    public BuddyRequestService(BuddyRequestRepository buddyRequestRepository,
                               UserRepository userRepository) {
        this.buddyRequestRepository = buddyRequestRepository;
        this.userRepository = userRepository;
    }

    public BuddyRequest sendRequest(Long requesterId, Long receiverId) {
        if (requesterId.equals(receiverId)) {
            throw new IllegalArgumentException("Cannot send request to yourself");
        }

        Users requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("Requester not found"));
        Users receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        BuddyRequest request = new BuddyRequest();
        request.setRequester(requester);
        request.setReceiver(receiver);
        request.setStatus("PENDING");
        request.setDate(LocalDate.now().toString());

        return buddyRequestRepository.save(request);
    }


}
