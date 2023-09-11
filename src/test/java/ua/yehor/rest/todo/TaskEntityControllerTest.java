package ua.yehor.rest.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ua.yehor.rest.todo.controller.TaskController;
import ua.yehor.rest.todo.dto.TaskDTO;
import ua.yehor.rest.todo.model.TaskEntity;
import ua.yehor.rest.todo.model.UserEntity;
import ua.yehor.rest.todo.service.TaskService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TaskEntityControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTasks() {
        List<TaskEntity> taskEntities = Arrays.asList(
                new TaskEntity(new UserEntity(), "Name1", "Description1", LocalDateTime.now()),
                new TaskEntity(new UserEntity(), "Name2", "Description2", LocalDateTime.now()));

        when(taskService.findAllByUserId()).thenReturn(taskEntities);

        List<TaskEntity> resultTaskEntities = taskController.getAllTasks().getBody();

        assertEquals(taskEntities, resultTaskEntities);
    }

    @Test
    void testGetTask() {
        TaskEntity taskEntity = new TaskEntity(new UserEntity(), "Name1", "Description1", LocalDateTime.now());

        when(taskService.findByName("Name1")).thenReturn(taskEntity);

        TaskEntity resultTaskEntity = taskController.getTask("Name1").getBody();

        assertEquals(taskEntity, resultTaskEntity);
    }

    @Test
    void testCreateTask() {
        TaskDTO taskDTO = new TaskDTO("Name", "Description", LocalDateTime.now());

        TaskEntity savedTaskEntity = new TaskEntity(new UserEntity(), "Name", "Description", LocalDateTime.now());
        when(taskService.createTask(taskDTO)).thenReturn(savedTaskEntity);

        ResponseEntity<TaskEntity> responseEntity = taskController.createTask(taskDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(savedTaskEntity, responseEntity.getBody());
    }

    @Test
    void testUpdateTask() {
        TaskDTO taskDTO = new TaskDTO("Updated name", "Description", LocalDateTime.now());

        TaskEntity updatedTaskEntity = new TaskEntity(new UserEntity(), "Updated name", "Description", LocalDateTime.now());
        when(taskService.updateTask("Name1", taskDTO)).thenReturn(updatedTaskEntity);

        ResponseEntity<TaskEntity> responseEntity = taskController.updateTask("Name1", taskDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedTaskEntity, responseEntity.getBody());
    }

    @Test
    void testSetNextWorkflowStatus() {
        TaskEntity updatedTaskEntity = new TaskEntity(new UserEntity(), "Updated name", "Description", LocalDateTime.now());

        when(taskService.setNextWorkflowStatus("Name", "POSTPONED")).thenReturn(updatedTaskEntity);

        ResponseEntity<TaskEntity> responseEntity = taskController.setNextWorkflowStatus("Name", "POSTPONED");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedTaskEntity, responseEntity.getBody());
    }

    @Test
    void testDeleteTask() {
        ResponseEntity<Void> responseEntity = taskController.deleteTask("Name");

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(taskService, times(1)).deleteByName("Name");
    }
}