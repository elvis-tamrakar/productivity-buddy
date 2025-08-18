package com.example.productivity_app.repository;

import com.example.productivity_app.entity.BuddyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuddyRequestRepository extends JpaRepository<BuddyRequest, Long> {
    List<BuddyRequest> findByRequester_Id(Long requesterId);
    List<BuddyRequest> findByReceiver_Id(Long receiverId);
    
    boolean existsByRequester_IdAndReceiver_Id(Long requesterId, Long receiverId);
    
    List<BuddyRequest> findByReceiver_IdAndStatus(Long receiverId, String status);
    
    @Query("SELECT br FROM BuddyRequest br WHERE (br.requester.id = :userId OR br.receiver.id = :userId) AND br.status = :status")
    List<BuddyRequest> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);
}