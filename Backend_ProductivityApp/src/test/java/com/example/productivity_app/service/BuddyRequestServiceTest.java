package com.example.productivity_app.service;

import com.example.productivity_app.entity.BuddyRequest;
import com.example.productivity_app.entity.Users;
import com.example.productivity_app.repository.BuddyRequestRepository;
import com.example.productivity_app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BuddyRequestService Unit Tests")
class BuddyRequestServiceTest {

    @Mock
    private BuddyRequestRepository buddyRequestRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BuddyRequestService buddyRequestService;

    private Users sender;
    private Users receiver;
    private BuddyRequest pendingRequest;
    private BuddyRequest acceptedRequest;
    private BuddyRequest rejectedRequest;

    @BeforeEach
    void setUp() {
        sender = new Users();
        sender.setId(1L);
        sender.setEmail("sender@example.com");
        sender.setUsername("sender");

        receiver = new Users();
        receiver.setId(2L);
        receiver.setEmail("receiver@example.com");
        receiver.setUsername("receiver");

        pendingRequest = new BuddyRequest();
        pendingRequest.setId(1L);
        pendingRequest.setRequester(sender);
        pendingRequest.setReceiver(receiver);
        pendingRequest.setStatus("PENDING");
        pendingRequest.setDate(LocalDate.now());

        acceptedRequest = new BuddyRequest();
        acceptedRequest.setId(1L);
        acceptedRequest.setRequester(sender);
        acceptedRequest.setReceiver(receiver);
        acceptedRequest.setStatus("ACCEPTED");
        acceptedRequest.setDate(LocalDate.now());

        rejectedRequest = new BuddyRequest();
        rejectedRequest.setId(1L);
        rejectedRequest.setRequester(sender);
        rejectedRequest.setReceiver(receiver);
        rejectedRequest.setStatus("REJECTED");
        rejectedRequest.setDate(LocalDate.now());
    }

    @Test
    @DisplayName("Should send buddy request successfully")
    void shouldSendBuddyRequestSuccessfully() {
        // Arrange
        when(buddyRequestRepository.existsByRequester_IdAndReceiver_Id(1L, 2L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));
        when(buddyRequestRepository.save(any(BuddyRequest.class))).thenReturn(pendingRequest);

        // Act
        BuddyRequest result = buddyRequestService.sendRequest(1L, 2L);

        // Assert
        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
        assertEquals(sender, result.getRequester());
        assertEquals(receiver, result.getReceiver());
        assertEquals(LocalDate.now(), result.getDate());
        verify(buddyRequestRepository).existsByRequester_IdAndReceiver_Id(1L, 2L);
        verify(userRepository).findById(1L);
        verify(userRepository).findById(2L);
        verify(buddyRequestRepository).save(any(BuddyRequest.class));
    }

    @Test
    @DisplayName("Should throw exception when sending request to yourself")
    void shouldThrowExceptionWhenSendingRequestToYourself() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> buddyRequestService.sendRequest(1L, 1L));
        assertEquals("Cannot send request to yourself", exception.getMessage());
        verify(buddyRequestRepository, never()).existsByRequester_IdAndReceiver_Id(any(), any());
        verify(userRepository, never()).findById(any());
        verify(buddyRequestRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when buddy request already exists")
    void shouldThrowExceptionWhenBuddyRequestAlreadyExists() {
        // Arrange
        when(buddyRequestRepository.existsByRequester_IdAndReceiver_Id(1L, 2L)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> buddyRequestService.sendRequest(1L, 2L));
        assertEquals("Buddy request already exists", exception.getMessage());
        verify(buddyRequestRepository).existsByRequester_IdAndReceiver_Id(1L, 2L);
        verify(userRepository, never()).findById(any());
        verify(buddyRequestRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when requester not found")
    void shouldThrowExceptionWhenRequesterNotFound() {
        // Arrange
        when(buddyRequestRepository.existsByRequester_IdAndReceiver_Id(1L, 2L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> buddyRequestService.sendRequest(1L, 2L));
        assertEquals("Requester not found", exception.getMessage());
        verify(buddyRequestRepository).existsByRequester_IdAndReceiver_Id(1L, 2L);
        verify(userRepository).findById(1L);
        verify(userRepository, never()).findById(2L);
        verify(buddyRequestRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when receiver not found")
    void shouldThrowExceptionWhenReceiverNotFound() {
        // Arrange
        when(buddyRequestRepository.existsByRequester_IdAndReceiver_Id(1L, 2L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> buddyRequestService.sendRequest(1L, 2L));
        assertEquals("Receiver not found", exception.getMessage());
        verify(buddyRequestRepository).existsByRequester_IdAndReceiver_Id(1L, 2L);
        verify(userRepository).findById(1L);
        verify(userRepository).findById(2L);
        verify(buddyRequestRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should accept buddy request successfully")
    void shouldAcceptBuddyRequestSuccessfully() {
        // Arrange
        when(buddyRequestRepository.findById(1L)).thenReturn(Optional.of(pendingRequest));
        when(buddyRequestRepository.save(any(BuddyRequest.class))).thenReturn(acceptedRequest);

        // Act
        BuddyRequest result = buddyRequestService.acceptRequest(1L, 2L);

        // Assert
        assertNotNull(result);
        assertEquals("ACCEPTED", result.getStatus());
        verify(buddyRequestRepository).findById(1L);
        verify(buddyRequestRepository).save(pendingRequest);
    }

    @Test
    @DisplayName("Should throw exception when accepting request that doesn't belong to you")
    void shouldThrowExceptionWhenAcceptingRequestThatDoesntBelongToYou() {
        // Arrange
        when(buddyRequestRepository.findById(1L)).thenReturn(Optional.of(pendingRequest));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> buddyRequestService.acceptRequest(1L, 999L));
        assertEquals("You can only accept requests sent to you", exception.getMessage());
        verify(buddyRequestRepository).findById(1L);
        verify(buddyRequestRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when accepting non-pending request")
    void shouldThrowExceptionWhenAcceptingNonPendingRequest() {
        // Arrange
        when(buddyRequestRepository.findById(1L)).thenReturn(Optional.of(acceptedRequest));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> buddyRequestService.acceptRequest(1L, 2L));
        assertEquals("Request is not pending", exception.getMessage());
        verify(buddyRequestRepository).findById(1L);
        verify(buddyRequestRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should reject buddy request successfully")
    void shouldRejectBuddyRequestSuccessfully() {
        // Arrange
        when(buddyRequestRepository.findById(1L)).thenReturn(Optional.of(pendingRequest));
        when(buddyRequestRepository.save(any(BuddyRequest.class))).thenReturn(rejectedRequest);

        // Act
        BuddyRequest result = buddyRequestService.rejectRequest(1L, 2L);

        // Assert
        assertNotNull(result);
        assertEquals("REJECTED", result.getStatus());
        verify(buddyRequestRepository).findById(1L);
        verify(buddyRequestRepository).save(pendingRequest);
    }

    @Test
    @DisplayName("Should throw exception when rejecting request that doesn't belong to you")
    void shouldThrowExceptionWhenRejectingRequestThatDoesntBelongToYou() {
        // Arrange
        when(buddyRequestRepository.findById(1L)).thenReturn(Optional.of(pendingRequest));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> buddyRequestService.rejectRequest(1L, 999L));
        assertEquals("You can only reject requests sent to you", exception.getMessage());
        verify(buddyRequestRepository).findById(1L);
        verify(buddyRequestRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when rejecting non-pending request")
    void shouldThrowExceptionWhenRejectingNonPendingRequest() {
        // Arrange
        when(buddyRequestRepository.findById(1L)).thenReturn(Optional.of(rejectedRequest));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> buddyRequestService.rejectRequest(1L, 2L));
        assertEquals("Request is not pending", exception.getMessage());
        verify(buddyRequestRepository).findById(1L);
        verify(buddyRequestRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should get pending requests for user successfully")
    void shouldGetPendingRequestsForUserSuccessfully() {
        // Arrange
        List<BuddyRequest> expectedRequests = Arrays.asList(pendingRequest);
        when(buddyRequestRepository.findByReceiver_IdAndStatus(2L, "PENDING")).thenReturn(expectedRequests);

        // Act
        List<BuddyRequest> result = buddyRequestService.getPendingRequestsForUser(2L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pendingRequest, result.get(0));
        verify(buddyRequestRepository).findByReceiver_IdAndStatus(2L, "PENDING");
    }

    @Test
    @DisplayName("Should get sent requests for user successfully")
    void shouldGetSentRequestsForUserSuccessfully() {
        // Arrange
        List<BuddyRequest> expectedRequests = Arrays.asList(pendingRequest);
        when(buddyRequestRepository.findByRequester_Id(1L)).thenReturn(expectedRequests);

        // Act
        List<BuddyRequest> result = buddyRequestService.getSentRequestsForUser(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pendingRequest, result.get(0));
        verify(buddyRequestRepository).findByRequester_Id(1L);
    }

    @Test
    @DisplayName("Should get accepted buddies for user successfully")
    void shouldGetAcceptedBuddiesForUserSuccessfully() {
        // Arrange
        List<BuddyRequest> expectedRequests = Arrays.asList(acceptedRequest);
        when(buddyRequestRepository.findByUserIdAndStatus(1L, "ACCEPTED")).thenReturn(expectedRequests);

        // Act
        List<BuddyRequest> result = buddyRequestService.getAcceptedBuddiesForUser(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(acceptedRequest, result.get(0));
        verify(buddyRequestRepository).findByUserIdAndStatus(1L, "ACCEPTED");
    }

    @Test
    @DisplayName("Should return empty list when no pending requests exist")
    void shouldReturnEmptyListWhenNoPendingRequestsExist() {
        // Arrange
        when(buddyRequestRepository.findByReceiver_IdAndStatus(2L, "PENDING")).thenReturn(Arrays.asList());

        // Act
        List<BuddyRequest> result = buddyRequestService.getPendingRequestsForUser(2L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(buddyRequestRepository).findByReceiver_IdAndStatus(2L, "PENDING");
    }

    @Test
    @DisplayName("Should return empty list when no sent requests exist")
    void shouldReturnEmptyListWhenNoSentRequestsExist() {
        // Arrange
        when(buddyRequestRepository.findByRequester_Id(1L)).thenReturn(Arrays.asList());

        // Act
        List<BuddyRequest> result = buddyRequestService.getSentRequestsForUser(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(buddyRequestRepository).findByRequester_Id(1L);
    }

    @Test
    @DisplayName("Should return empty list when no accepted buddies exist")
    void shouldReturnEmptyListWhenNoAcceptedBuddiesExist() {
        // Arrange
        when(buddyRequestRepository.findByUserIdAndStatus(1L, "ACCEPTED")).thenReturn(Arrays.asList());

        // Act
        List<BuddyRequest> result = buddyRequestService.getAcceptedBuddiesForUser(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(buddyRequestRepository).findByUserIdAndStatus(1L, "ACCEPTED");
    }
}
