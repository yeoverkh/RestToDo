package ua.yehor.rest.todo.model.workflowstatus;

import java.util.EnumMap;
import java.util.Map;

public class WorkflowStatusGraph {

    private final Map<WorkflowStatus, WorkflowNode> nodes = new EnumMap<>(WorkflowStatus.class);

    public void addNode(WorkflowStatus status) {
        nodes.putIfAbsent(status, new WorkflowNode(status));
    }

    public WorkflowNode getNode(WorkflowStatus workflowStatus) {
        return nodes.get(workflowStatus);
    }

    public void addEdge(WorkflowStatus fromStatus, WorkflowStatus toStatus) {
        WorkflowNode fromNode = getNode(fromStatus);
        WorkflowNode toNode = getNode(toStatus);

        if (fromNode == null || toNode == null) {
            throw new IllegalArgumentException("Both fromStatus and toStatus should be present in the graph.");
        }

        fromNode.addNeighbor(toStatus);
    }
}