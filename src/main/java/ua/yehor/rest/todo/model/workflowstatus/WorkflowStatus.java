package ua.yehor.rest.todo.model.workflowstatus;

public enum WorkflowStatus {

    PLANNED, WORK_IN_PROGRESS, POSTPONED, NOTIFIED, SIGNED, DONE, CANCELLED;

    private static final WorkflowStatusGraph graph = new WorkflowStatusGraph();

    static {
        for (WorkflowStatus workflowStatus : WorkflowStatus.values()) {
            graph.addNode(workflowStatus);
        }

        graph.addEdge(PLANNED, WORK_IN_PROGRESS);
        graph.addEdge(PLANNED, POSTPONED);
        graph.addEdge(WORK_IN_PROGRESS, NOTIFIED);
        graph.addEdge(WORK_IN_PROGRESS, SIGNED);
        graph.addEdge(POSTPONED, NOTIFIED);
        graph.addEdge(POSTPONED, SIGNED);
        graph.addEdge(NOTIFIED, DONE);
        graph.addEdge(SIGNED, DONE);
    }

    public WorkflowStatus nextStatus(String nextStatus) {
        WorkflowStatus nextWorkflowStatus = WorkflowStatus.valueOf(nextStatus);

        if (this.equals(DONE) || this.equals(CANCELLED)) {
            throw new IllegalStateException("Status is already final: " + this);
        }

        if (nextWorkflowStatus.equals(CANCELLED)) {
            return CANCELLED;
        }

        if (!graph.getNode(this).getNeighbors().contains(nextWorkflowStatus)) {
            throw new IllegalStateException();
        }

        return nextWorkflowStatus;
    }
}
