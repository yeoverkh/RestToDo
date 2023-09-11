package ua.yehor.rest.todo.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.yehor.rest.todo.dto.TaskDTO;
import ua.yehor.rest.todo.model.TaskEntity;
import ua.yehor.rest.todo.model.UserEntity;
import ua.yehor.rest.todo.repository.TaskRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    public List<TaskEntity> findAllByUserId() {
        return taskRepository.findAllByUserEntityId(userService.getCurrentUser().getId());
    }

    public TaskEntity findByName(String name) {
        UserEntity userEntity = userService.getCurrentUser();

        TaskEntity foundTaskEntity = taskRepository.findByUserEntityAndName(userEntity, name);

        if (foundTaskEntity == null) {
            throw new EntityNotFoundException("Task with this name cannot be found");
        }
        return foundTaskEntity;
    }

    public TaskEntity updateTask(String name, TaskDTO taskDTO) {
        TaskEntity updatingTaskEntity = findByName(name);

        updatingTaskEntity.setName(taskDTO.name());
        updatingTaskEntity.setDescription(taskDTO.description());
        updatingTaskEntity.setEndDateTime(taskDTO.endDateTime());

        taskRepository.save(updatingTaskEntity);
        return updatingTaskEntity;
    }

    public TaskEntity setNextWorkflowStatus(String name, String status) {
        TaskEntity updatingTaskEntity = findByName(name);

        updatingTaskEntity.setNextWorkflowStatus(status);

        taskRepository.save(updatingTaskEntity);
        return updatingTaskEntity;
    }

    @Transactional
    public void deleteByName(String name) {
        taskRepository.deleteByName(name);
    }

    public TaskEntity createTask(TaskDTO taskDTO) {
        UserEntity userEntity = userService.getCurrentUser();

        if (taskRepository.findByUserEntityAndName(userEntity, taskDTO.name()) != null) {
            throw new EntityExistsException("Task with this name is already exists");
        }

        return taskRepository.save(new TaskEntity(userEntity, taskDTO.name(), taskDTO.description(), taskDTO.endDateTime()));
    }
}
