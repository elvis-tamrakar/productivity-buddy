package com.example.productivity_app.service;

import com.example.productivity_app.entity.Checkpoint;
import com.example.productivity_app.entity.Goal;
import com.example.productivity_app.entity.Users;
import com.example.productivity_app.repository.CheckpointRepository;
import com.example.productivity_app.repository.GoalRepository;
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
@DisplayName("CheckpointService Unit Tests")
class CheckpointServiceTest {

    @Mock
    private CheckpointRepository checkpointRepository;

    @Mock
    private GoalRepository goalRepository;

    @InjectMocks
    private CheckpointService checkpointService;

    private Users testUser;
    private Goal testGoal;
    private Checkpoint testCheckpoint;
    private Checkpoint updateCheckpointData;

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
        testGoal.setCheckpoints(new ArrayList<>());

        testCheckpoint = new Checkpoint();
        testCheckpoint.setId(1L);
        testCheckpoint.setTitle("Test Checkpoint");
        testCheckpoint.setDescription("Test Checkpoint Description");
        testCheckpoint.setDueDate(LocalDate.now().plusDays(7));
        testCheckpoint.setStatus("PENDING");
        testCheckpoint.setGoal(testGoal);

        updateCheckpointData = new Checkpoint();
        updateCheckpointData.setTitle("Updated Checkpoint");
        updateCheckpointData.setDescription("Updated Description");
        updateCheckpointData.setDueDate(LocalDate.now().plusDays(14));
        updateCheckpointData.setStatus("IN_PROGRESS");
    }

    @Test
    @DisplayName("Should add checkpoint to goal successfully")
    void shouldAddCheckpointToGoalSuccessfully() {
        // Arrange
        when(goalRepository.findById(1L)).thenReturn(Optional.of(testGoal));
        when(checkpointRepository.save(any(Checkpoint.class))).thenReturn(testCheckpoint);
        when(goalRepository.save(any(Goal.class))).thenReturn(testGoal);

        // Act
        Checkpoint result = checkpointService.addCheckpointToGoal(1L, testCheckpoint);

        // Assert
        assertNotNull(result);
        assertEquals(testGoal, result.getGoal());
        assertTrue(testGoal.getCheckpoints().contains(result));
        verify(goalRepository).findById(1L);
        verify(checkpointRepository).save(testCheckpoint);
        verify(goalRepository).save(testGoal);
    }

    @Test
    @DisplayName("Should throw exception when adding checkpoint to non-existent goal")
    void shouldThrowExceptionWhenAddingCheckpointToNonExistentGoal() {
        // Arrange
        when(goalRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> checkpointService.addCheckpointToGoal(999L, testCheckpoint));
        assertEquals("Goal not found", exception.getMessage());
        verify(goalRepository).findById(999L);
        verify(checkpointRepository, never()).save(any());
        verify(goalRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should get checkpoints for goal successfully")
    void shouldGetCheckpointsForGoalSuccessfully() {
        // Arrange
        List<Checkpoint> expectedCheckpoints = Arrays.asList(testCheckpoint);
        when(checkpointRepository.findByGoal_Id(1L)).thenReturn(expectedCheckpoints);

        // Act
        List<Checkpoint> result = checkpointService.getCheckpointsForGoal(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCheckpoint, result.get(0));
        verify(checkpointRepository).findByGoal_Id(1L);
    }

    @Test
    @DisplayName("Should return empty list when goal has no checkpoints")
    void shouldReturnEmptyListWhenGoalHasNoCheckpoints() {
        // Arrange
        when(checkpointRepository.findByGoal_Id(1L)).thenReturn(new ArrayList<>());

        // Act
        List<Checkpoint> result = checkpointService.getCheckpointsForGoal(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(checkpointRepository).findByGoal_Id(1L);
    }

    @Test
    @DisplayName("Should update checkpoint successfully with partial data")
    void shouldUpdateCheckpointSuccessfullyWithPartialData() {
        // Arrange
        when(checkpointRepository.findById(1L)).thenReturn(Optional.of(testCheckpoint));
        when(checkpointRepository.save(any(Checkpoint.class))).thenReturn(testCheckpoint);
        when(goalRepository.save(any(Goal.class))).thenReturn(testGoal);

        // Act
        Checkpoint result = checkpointService.updateCheckpoint(1L, updateCheckpointData);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Checkpoint", testCheckpoint.getTitle());
        assertEquals("Updated Description", testCheckpoint.getDescription());
        assertEquals(LocalDate.now().plusDays(14), testCheckpoint.getDueDate());
        assertEquals("IN_PROGRESS", testCheckpoint.getStatus());
        verify(checkpointRepository).findById(1L);
        verify(checkpointRepository).save(testCheckpoint);
        verify(goalRepository).save(testGoal);
    }

    @Test
    @DisplayName("Should update checkpoint successfully with null values (no changes)")
    void shouldUpdateCheckpointSuccessfullyWithNullValues() {
        // Arrange
        Checkpoint nullUpdateData = new Checkpoint();
        nullUpdateData.setTitle(null);
        nullUpdateData.setDescription(null);
        nullUpdateData.setDueDate(null);
        nullUpdateData.setStatus(null);

        when(checkpointRepository.findById(1L)).thenReturn(Optional.of(testCheckpoint));
        when(checkpointRepository.save(any(Checkpoint.class))).thenReturn(testCheckpoint);
        when(goalRepository.save(any(Goal.class))).thenReturn(testGoal);

        // Act
        Checkpoint result = checkpointService.updateCheckpoint(1L, nullUpdateData);

        // Assert
        assertNotNull(result);
        assertEquals("Test Checkpoint", testCheckpoint.getTitle()); // Original value unchanged
        assertEquals("Test Checkpoint Description", testCheckpoint.getDescription()); // Original value unchanged
        verify(checkpointRepository).findById(1L);
        verify(checkpointRepository).save(testCheckpoint);
        verify(goalRepository).save(testGoal);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent checkpoint")
    void shouldThrowExceptionWhenUpdatingNonExistentCheckpoint() {
        // Arrange
        when(checkpointRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> checkpointService.updateCheckpoint(999L, updateCheckpointData));
        assertEquals("Checkpoint not found", exception.getMessage());
        verify(checkpointRepository).findById(999L);
        verify(checkpointRepository, never()).save(any());
        verify(goalRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should complete checkpoint successfully")
    void shouldCompleteCheckpointSuccessfully() {
        // Arrange
        when(checkpointRepository.findById(1L)).thenReturn(Optional.of(testCheckpoint));
        when(checkpointRepository.save(any(Checkpoint.class))).thenReturn(testCheckpoint);
        when(goalRepository.save(any(Goal.class))).thenReturn(testGoal);

        // Act
        Checkpoint result = checkpointService.completeCheckpoint(1L);

        // Assert
        assertNotNull(result);
        assertEquals("COMPLETED", testCheckpoint.getStatus());
        assertEquals(LocalDate.now(), testCheckpoint.getCompletedDate());
        verify(checkpointRepository).findById(1L);
        verify(checkpointRepository).save(testCheckpoint);
        verify(goalRepository).save(testGoal);
    }

    @Test
    @DisplayName("Should throw exception when completing non-existent checkpoint")
    void shouldThrowExceptionWhenCompletingNonExistentCheckpoint() {
        // Arrange
        when(checkpointRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> checkpointService.completeCheckpoint(999L));
        assertEquals("Checkpoint not found", exception.getMessage());
        verify(checkpointRepository).findById(999L);
        verify(checkpointRepository, never()).save(any());
        verify(goalRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete checkpoint successfully")
    void shouldDeleteCheckpointSuccessfully() {
        // Arrange
        testGoal.getCheckpoints().add(testCheckpoint);
        when(checkpointRepository.findById(1L)).thenReturn(Optional.of(testCheckpoint));
        doNothing().when(checkpointRepository).delete(testCheckpoint);
        when(goalRepository.save(any(Goal.class))).thenReturn(testGoal);

        // Act
        checkpointService.deleteCheckpoint(1L);

        // Assert
        assertFalse(testGoal.getCheckpoints().contains(testCheckpoint));
        verify(checkpointRepository).findById(1L);
        verify(checkpointRepository).delete(testCheckpoint);
        verify(goalRepository).save(testGoal);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent checkpoint")
    void shouldThrowExceptionWhenDeletingNonExistentCheckpoint() {
        // Arrange
        when(checkpointRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> checkpointService.deleteCheckpoint(999L));
        assertEquals("Checkpoint not found", exception.getMessage());
        verify(checkpointRepository).findById(999L);
        verify(checkpointRepository, never()).delete(any());
        verify(goalRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update goal progress when checkpoint is completed")
    void shouldUpdateGoalProgressWhenCheckpointIsCompleted() {
        // Arrange
        testGoal.getCheckpoints().add(testCheckpoint);
        when(checkpointRepository.findById(1L)).thenReturn(Optional.of(testCheckpoint));
        when(checkpointRepository.save(any(Checkpoint.class))).thenReturn(testCheckpoint);
        when(goalRepository.save(any(Goal.class))).thenReturn(testGoal);

        // Act
        Checkpoint result = checkpointService.completeCheckpoint(1L);

        // Assert
        assertNotNull(result);
        assertEquals("COMPLETED", testCheckpoint.getStatus());
        verify(goalRepository).save(testGoal);
        // The goal's calculateProgress method should have been called
        // We can verify this indirectly by checking that the goal was saved
    }

    @Test
    @DisplayName("Should update goal progress when checkpoint is deleted")
    void shouldUpdateGoalProgressWhenCheckpointIsDeleted() {
        // Arrange
        testGoal.getCheckpoints().add(testCheckpoint);
        when(checkpointRepository.findById(1L)).thenReturn(Optional.of(testCheckpoint));
        doNothing().when(checkpointRepository).delete(testCheckpoint);
        when(goalRepository.save(any(Goal.class))).thenReturn(testGoal);

        // Act
        checkpointService.deleteCheckpoint(1L);

        // Assert
        assertFalse(testGoal.getCheckpoints().contains(testCheckpoint));
        verify(goalRepository).save(testGoal);
        // The goal's calculateProgress method should have been called
        // We can verify this indirectly by checking that the goal was saved
    }
}
