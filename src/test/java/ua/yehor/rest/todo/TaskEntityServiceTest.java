package ua.yehor.rest.todo;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.yehor.rest.todo.dto.TaskDTO;
import ua.yehor.rest.todo.model.TaskEntity;
import ua.yehor.rest.todo.model.UserEntity;
import ua.yehor.rest.todo.model.workflowstatus.WorkflowStatus;
import ua.yehor.rest.todo.repository.TaskRepository;
import ua.yehor.rest.todo.service.TaskService;
import ua.yehor.rest.todo.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskEntityServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllByUserId() {
        UserEntity currentUserEntity = new UserEntity("user", "pass");
        when(userService.getCurrentUser()).thenReturn(currentUserEntity);

        List<TaskEntity> taskEntities = new ArrayList<>();
        taskEntities.add(new TaskEntity(currentUserEntity, "Task 1", "Description 1", null));
        taskEntities.add(new TaskEntity(currentUserEntity, "Task 2", "Description 2", null));

        when(taskRepository.findAllByUserEntityId(currentUserEntity.getId())).thenReturn(taskEntities);

        List<TaskEntity> result = taskService.findAllByUserId();

        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getName());
        assertEquals("Task 2", result.get(1).getName());
    }

    @Test
    void testFindByName_TaskFound() {
        UserEntity currentUserEntity = new UserEntity("user", "pass");
        when(userService.getCurrentUser()).thenReturn(currentUserEntity);

        TaskEntity foundTaskEntity = new TaskEntity(currentUserEntity, "Task 1", "Description 1", null);
        when(taskRepository.findByUserEntityAndName(currentUserEntity, "Task 1")).thenReturn(foundTaskEntity);

        TaskEntity result = taskService.findByName("Task 1");

        assertNotNull(result);
        assertEquals("Task 1", result.getName());
    }

    @Test
    void testFindByName_TaskNotFound() {
        UserEntity currentUserEntity = new UserEntity("user", "pass");
        when(userService.getCurrentUser()).thenReturn(currentUserEntity);

        when(taskRepository.findByUserEntityAndName(currentUserEntity, "Nonexistent Task")).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> taskService.findByName("Nonexistent Task"));
    }

    @Test
    void testUpdateTask() {
        UserEntity currentUserEntity = new UserEntity("user", "pass");
        when(userService.getCurrentUser()).thenReturn(currentUserEntity);

        TaskEntity foundTaskEntity = new TaskEntity(currentUserEntity, "Task 1", "Description 1", null);
        when(taskRepository.findByUserEntityAndName(currentUserEntity, "Task 1")).thenReturn(foundTaskEntity);

        TaskDTO taskDTO = new TaskDTO("Updated Task", "Updated Description", null);
        TaskEntity updatedTaskEntity = taskService.updateTask("Task 1", taskDTO);

        assertNotNull(updatedTaskEntity);
        assertEquals("Updated Task", updatedTaskEntity.getName());
        assertEquals("Updated Description", updatedTaskEntity.getDescription());
    }

    @Test
    void testSetNextWorkflowStatus_Cancelled() {
        UserEntity currentUserEntity = new UserEntity("user", "pass");
        when(userService.getCurrentUser()).thenReturn(currentUserEntity);

        TaskEntity foundTaskEntity = new TaskEntity(currentUserEntity, "Task 1", "Description 1", null);
        when(taskRepository.findByUserEntityAndName(currentUserEntity, "Task 1")).thenReturn(foundTaskEntity);

        TaskEntity updatedTaskEntity = taskService.setNextWorkflowStatus("Task 1", "CANCELLED");

        assertNotNull(updatedTaskEntity);
        assertEquals(WorkflowStatus.CANCELLED, updatedTaskEntity.getWorkflowStatus());
    }

    @Test
    void testSetNextWorkflowStatus_NonCancelled() {
        UserEntity currentUserEntity = new UserEntity("user", "pass");
        when(userService.getCurrentUser()).thenReturn(currentUserEntity);

        TaskEntity foundTaskEntity = new TaskEntity(currentUserEntity, "Task 1", "Description 1", null);
        when(taskRepository.findByUserEntityAndName(currentUserEntity, "Task 1")).thenReturn(foundTaskEntity);

        TaskEntity updatedTaskEntity = taskService.setNextWorkflowStatus("Task 1", "POSTPONED");

        assertNotNull(updatedTaskEntity);
        assertNotEquals(WorkflowStatus.CANCELLED, updatedTaskEntity.getWorkflowStatus());
    }

    @Test
    void testDeleteByName() {
        taskService.deleteByName("TaskToDelete");

        verify(taskRepository, times(1)).deleteByName("TaskToDelete");
    }

    @Test
    void testCreateTask_TaskDoesNotExist() {
        UserEntity currentUserEntity = new UserEntity("user", "pass");
        when(userService.getCurrentUser()).thenReturn(currentUserEntity);

        TaskDTO taskDTO = new TaskDTO("New Task", "New Description", null);
        when(taskRepository.findByUserEntityAndName(currentUserEntity, "New Task")).thenReturn(null);

        TaskEntity newTaskEntity = new TaskEntity(currentUserEntity, "New Task", "New Description", null);
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(newTaskEntity);

        TaskEntity createdTaskEntity = taskService.createTask(taskDTO);

        assertNotNull(createdTaskEntity);
        assertEquals("New Task", createdTaskEntity.getName());
        assertEquals("New Description", createdTaskEntity.getDescription());
    }

    @Test
    void testCreateTask_TaskAlreadyExists() {
        UserEntity currentUserEntity = new UserEntity("user", "pass");
        when(userService.getCurrentUser()).thenReturn(currentUserEntity);

        TaskDTO taskDTO = new TaskDTO("Existing Task", "Existing Description", null);
        when(taskRepository.findByUserEntityAndName(currentUserEntity, "Existing Task")).thenReturn(new TaskEntity());

        assertThrows(EntityExistsException.class, () -> taskService.createTask(taskDTO));
    }
}