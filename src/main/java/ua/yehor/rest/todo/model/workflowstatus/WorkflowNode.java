package ua.yehor.rest.todo.model.workflowstatus;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class WorkflowNode {

    private final WorkflowStatus workflowStatus;
    private final List<WorkflowStatus> neighbors = new ArrayList<>();

    WorkflowNode(WorkflowStatus workflowStatus) {
        this.workflowStatus = workflowStatus;
    }

    void addNeighbor(WorkflowStatus neighbor) {
        neighbors.add(neighbor);
    }
}