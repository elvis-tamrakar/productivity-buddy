package com.example.productivity_app.repository;

import com.example.productivity_app.entity.BuddyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuddyRequestRepository extends JpaRepository<BuddyRequest, Long> {
    List<BuddyRequest> findByRequester_Id(Long requesterId);
    List<BuddyRequest> findByReceiver_Id(Long receiverId);
}