package com.example.productivity_app.service;

import com.example.productivity_app.entity.Checkpoint;
import com.example.productivity_app.entity.Goal;
import com.example.productivity_app.entity.Users;
import com.example.productivity_app.repository.GoalRepository;
import com.example.productivity_app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GoalService Unit Tests")
class GoalServiceTest {

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GoalService goalService;

    private Users testUser;
    private Goal testGoal;
    private Goal testGoalWithCheckpoints;
    private Checkpoint completedCheckpoint;
    private Checkpoint pendingCheckpoint;

    @BeforeEach
    void setUp() {
        testUser = new Users();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setUsername("testuser");

        testGoal = new Goal();
        testGoal.setId(1L);
        testGoal.setTitle("Test Goal");
        testGoal.setDescription("Test Description");
        testGoal.setStartDate(LocalDate.now());
        testGoal.setEndDate(LocalDate.now().plusDays(30));
        testGoal.setStatus("ACTIVE");
        testGoal.setProgress(0);
        testGoal.setUser(testUser);

        // Create checkpoints for testing progress calculation
        completedCheckpoint = new Checkpoint();
        completedCheckpoint.setId(1L);
        completedCheckpoint.setTitle("Completed Checkpoint");
        completedCheckpoint.setStatus("COMPLETED");
        completedCheckpoint.setGoal(testGoalWithCheckpoints);

        pendingCheckpoint = new Checkpoint();
        pendingCheckpoint.setId(2L);
        pendingCheckpoint.setTitle("Pending Checkpoint");
        pendingCheckpoint.setStatus("PENDING");
        pendingCheckpoint.setGoal(testGoalWithCheckpoints);

        testGoalWithCheckpoints = new Goal();
        testGoalWithCheckpoints.setId(2L);
        testGoalWithCheckpoints.setTitle("Goal with Checkpoints");
        testGoalWithCheckpoints.setDescription("Goal with Checkpoints Description");
        testGoalWithCheckpoints.setStartDate(LocalDate.now());
        testGoalWithCheckpoints.setEndDate(LocalDate.now().plusDays(30));
        testGoalWithCheckpoints.setStatus("ACTIVE");
        testGoalWithCheckpoints.setProgress(0);
        testGoalWithCheckpoints.setUser(testUser);
        testGoalWithCheckpoints.setCheckpoints(Arrays.asList(completedCheckpoint, pendingCheckpoint));
    }

    @Test
    @DisplayName("Should create goal successfully")
    void shouldCreateGoalSuccessfully() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(goalRepository.save(any(Goal.class))).thenReturn(testGoal);

        // Act
        Goal result = goalService.createGoal(1L, testGoal);

        // Assert
        assertNotNull(result);
        assertEquals(testUser, result.getUsers());
        verify(userRepository).findById(1L);
        verify(goalRepository).save(testGoal);
    }

    @Test
    @DisplayName("Should throw exception when creating goal with non-existent user")
    void shouldThrowExceptionWhenCreatingGoalWithNonExistentUser() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> goalService.createGoal(999L, testGoal));
        verify(userRepository).findById(999L);
        verify(goalRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should get user goals successfully")
    void shouldGetUserGoalsSuccessfully() {
        // Arrange
        List<Goal> expectedGoals = Arrays.asList(testGoal);
        when(goalRepository.findByUsersId(1L)).thenReturn(expectedGoals);

        // Act
        List<Goal> result = goalService.getUserGoals(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testGoal, result.get(0));
        verify(goalRepository).findByUsersId(1L);
    }

    @Test
    @DisplayName("Should return empty list when user has no goals")
    void shouldReturnEmptyListWhenUserHasNoGoals() {
        // Arrange
        when(goalRepository.findByUsersId(1L)).thenReturn(new ArrayList<>());

        // Act
        List<Goal> result = goalService.getUserGoals(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(goalRepository).findByUsersId(1L);
    }

    @Test
    @DisplayName("Should get goal by ID successfully")
    void shouldGetGoalByIdSuccessfully() {
        // Arrange
        when(goalRepository.findById(1L)).thenReturn(Optional.of(testGoal));

        // Act
        Goal result = goalService.getGoalById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testGoal, result);
        verify(goalRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when goal not found by ID")
    void shouldThrowExceptionWhenGoalNotFoundById() {
        // Arrange
        when(goalRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> goalService.getGoalById(999L));
        assertEquals("Goal not found", exception.getMessage());
        verify(goalRepository).findById(999L);
    }

    @Test
    @DisplayName("Should update goal successfully with partial data")
    void shouldUpdateGoalSuccessfullyWithPartialData() {
        // Arrange
        Goal updateData = new Goal();
        updateData.setTitle("Updated Title");
        updateData.setDescription("Updated Description");

        when(goalRepository.findById(1L)).thenReturn(Optional.of(testGoal));
        when(goalRepository.save(any(Goal.class))).thenReturn(testGoal);

        // Act
        Goal result = goalService.updateGoal(1L, updateData);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Title", testGoal.getTitle());
        assertEquals("Updated Description", testGoal.getDescription());
        verify(goalRepository).findById(1L);
        verify(goalRepository).save(testGoal);
    }

    @Test
    @DisplayName("Should update goal successfully with null values (no changes)")
    void shouldUpdateGoalSuccessfullyWithNullValues() {
        // Arrange
        Goal updateData = new Goal();
        updateData.setTitle(null);
        updateData.setDescription(null);

        when(goalRepository.findById(1L)).thenReturn(Optional.of(testGoal));
        when(goalRepository.save(any(Goal.class))).thenReturn(testGoal);

        // Act
        Goal result = goalService.updateGoal(1L, updateData);

        // Assert
        assertNotNull(result);
        assertEquals("Test Goal", testGoal.getTitle()); // Original value unchanged
        assertEquals("Test Description", testGoal.getDescription()); // Original value unchanged
        verify(goalRepository).findById(1L);
        verify(goalRepository).save(testGoal);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent goal")
    void shouldThrowExceptionWhenUpdatingNonExistentGoal() {
        // Arrange
        Goal updateData = new Goal();
        when(goalRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> goalService.updateGoal(999L, updateData));
        assertEquals("Goal not found", exception.getMessage());
        verify(goalRepository).findById(999L);
        verify(goalRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete goal successfully")
    void shouldDeleteGoalSuccessfully() {
        // Arrange
        when(goalRepository.findById(1L)).thenReturn(Optional.of(testGoal));
        doNothing().when(goalRepository).delete(testGoal);

        // Act
        goalService.deleteGoal(1L);

        // Assert
        verify(goalRepository).findById(1L);
        verify(goalRepository).delete(testGoal);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent goal")
    void shouldThrowExceptionWhenDeletingNonExistentGoal() {
        // Arrange
        when(goalRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> goalService.deleteGoal(999L));
        assertEquals("Goal not found", exception.getMessage());
        verify(goalRepository).findById(999L);
        verify(goalRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should complete goal successfully")
    void shouldCompleteGoalSuccessfully() {
        // Arrange
        when(goalRepository.findById(1L)).thenReturn(Optional.of(testGoal));
        when(goalRepository.save(any(Goal.class))).thenReturn(testGoal);

        // Act
        Goal result = goalService.completeGoal(1L);

        // Assert
        assertNotNull(result);
        assertEquals("COMPLETED", testGoal.getStatus());
        assertEquals(100, testGoal.getProgress());
        verify(goalRepository).findById(1L);
        verify(goalRepository).save(testGoal);
    }

    @Test
    @DisplayName("Should throw exception when completing non-existent goal")
    void shouldThrowExceptionWhenCompletingNonExistentGoal() {
        // Arrange
        when(goalRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> goalService.completeGoal(999L));
        assertEquals("Goal not found", exception.getMessage());
        verify(goalRepository).findById(999L);
        verify(goalRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should calculate progress correctly with mixed checkpoint statuses")
    void shouldCalculateProgressCorrectlyWithMixedCheckpointStatuses() {
        // Arrange
        Goal goal = new Goal();
        goal.setCheckpoints(Arrays.asList(completedCheckpoint, pendingCheckpoint));
        
        // Act
        goal.calculateProgress();
        
        // Assert
        assertEquals(50, goal.getProgress()); // 1 out of 2 checkpoints completed = 50%
    }

    @Test
    @DisplayName("Should calculate progress as 0 when no checkpoints exist")
    void shouldCalculateProgressAsZeroWhenNoCheckpointsExist() {
        // Arrange
        Goal goal = new Goal();
        goal.setCheckpoints(new ArrayList<>());
        
        // Act
        goal.calculateProgress();
        
        // Assert
        assertEquals(0, goal.getProgress());
    }

    @Test
    @DisplayName("Should calculate progress as 100 when all checkpoints are completed")
    void shouldCalculateProgressAsHundredWhenAllCheckpointsCompleted() {
        // Arrange
        Checkpoint secondCompletedCheckpoint = new Checkpoint();
        secondCompletedCheckpoint.setStatus("COMPLETED");
        
        Goal goal = new Goal();
        goal.setCheckpoints(Arrays.asList(completedCheckpoint, secondCompletedCheckpoint));
        
        // Act
        goal.calculateProgress();
        
        // Assert
        assertEquals(100, goal.getProgress()); // 2 out of 2 checkpoints completed = 100%
    }
}
