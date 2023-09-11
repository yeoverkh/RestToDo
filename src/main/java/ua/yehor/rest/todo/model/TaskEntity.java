package ua.yehor.rest.todo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.yehor.rest.todo.model.workflowstatus.WorkflowStatus;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
    @ManyToOne
    private UserEntity userEntity;
    private String name;
    private String description;
    private WorkflowStatus workflowStatus;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public TaskEntity(UserEntity userEntity, String name, String description, LocalDateTime endDateTime) {
        this.userEntity = userEntity;
        this.workflowStatus = WorkflowStatus.PLANNED;
        this.name = name;
        this.description = description;
        this.startDateTime = LocalDateTime.now();
        this.endDateTime = endDateTime;
    }

    public void setNextWorkflowStatus(String status) {
        this.workflowStatus = workflowStatus.nextStatus(status);
    }

    public void setName(String name) {
        if (name.isBlank()) {
            return;
        }
        this.name = name;
    }

    public void setDescription(String description) {
        if (name.isBlank()) {
            return;
        }
        this.description = description;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }
}
