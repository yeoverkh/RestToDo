package ua.yehor.rest.todo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.yehor.rest.todo.dto.TaskDTO;
import ua.yehor.rest.todo.model.TaskEntity;
import ua.yehor.rest.todo.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskEntity>> getAllTasks() {
        return ResponseEntity.ok(taskService.findAllByUserId());
    }

    @GetMapping("/{name}")
    public ResponseEntity<TaskEntity> getTask(@PathVariable String name) {
        return ResponseEntity.ok(taskService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<TaskEntity> createTask(@Validated @RequestBody TaskDTO taskDTO) {
        TaskEntity savedTaskEntity = taskService.createTask(taskDTO);

        return new ResponseEntity<>(savedTaskEntity, HttpStatus.CREATED);
    }

    @PutMapping("/{name}")
    public ResponseEntity<TaskEntity> updateTask(@PathVariable String name, @Validated @RequestBody TaskDTO taskDTO) {
        TaskEntity updatingTaskEntity = taskService.updateTask(name, taskDTO);

        return ResponseEntity.ok(updatingTaskEntity);
    }

    @PatchMapping("/{name}")
    public ResponseEntity<TaskEntity> setNextWorkflowStatus(@PathVariable String name, @RequestParam String status) {
        TaskEntity updatingTaskEntity = taskService.setNextWorkflowStatus(name, status);

        return ResponseEntity.ok(updatingTaskEntity);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteTask(@PathVariable String name) {
        taskService.deleteByName(name);

        return ResponseEntity.noContent().build();
    }
}
