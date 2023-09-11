package ua.yehor.rest.todo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ua.yehor.rest.todo.model.workflowstatus.WorkflowStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WorkflowStatusTest {

    @Test
    void testNextStatus_PlannedToPostponed() {
        WorkflowStatus currentStatus = WorkflowStatus.PLANNED;
        assertEquals(WorkflowStatus.POSTPONED, currentStatus.nextStatus("POSTPONED"));
    }

    @Test
    void testNextStatus_PlannedToWorkInProgress() {
        WorkflowStatus currentStatus = WorkflowStatus.PLANNED;
        assertEquals(WorkflowStatus.WORK_IN_PROGRESS, currentStatus.nextStatus("WORK_IN_PROGRESS"));
    }

    @Test
    void testNextStatus_WorkInProgressToNotified() {
        WorkflowStatus currentStatus = WorkflowStatus.WORK_IN_PROGRESS;
        assertEquals(WorkflowStatus.NOTIFIED, currentStatus.nextStatus("NOTIFIED"));
    }

    @Test
    void testNextStatus_WorkInProgressToSigned() {
        WorkflowStatus currentStatus = WorkflowStatus.WORK_IN_PROGRESS;
        assertEquals(WorkflowStatus.SIGNED, currentStatus.nextStatus("SIGNED"));
    }

    @Test
    void testNextStatus_PostponedToNotified() {
        WorkflowStatus currentStatus = WorkflowStatus.POSTPONED;
        assertEquals(WorkflowStatus.NOTIFIED, currentStatus.nextStatus("NOTIFIED"));
    }

    @Test
    void testNextStatus_PostponedToSigned() {
        WorkflowStatus currentStatus = WorkflowStatus.POSTPONED;
        assertEquals(WorkflowStatus.SIGNED, currentStatus.nextStatus("SIGNED"));
    }

    @Test
    void testNextStatus_NotifiedToDone() {
        WorkflowStatus currentStatus = WorkflowStatus.NOTIFIED;
        assertEquals(WorkflowStatus.DONE, currentStatus.nextStatus("DONE"));
    }

    @Test
    void testNextStatus_SignedToDone() {
        WorkflowStatus currentStatus = WorkflowStatus.SIGNED;
        assertEquals(WorkflowStatus.DONE, currentStatus.nextStatus("DONE"));
    }

    @Test
    void testNextStatus_PlannedToCancelled() {
        WorkflowStatus currentStatus = WorkflowStatus.PLANNED;
        assertEquals(WorkflowStatus.CANCELLED, currentStatus.nextStatus("CANCELLED"));
    }

    @Test
    void testNextStatus_PostponedToCancelled() {
        WorkflowStatus currentStatus = WorkflowStatus.POSTPONED;
        assertEquals(WorkflowStatus.CANCELLED, currentStatus.nextStatus("CANCELLED"));
    }

    @Test
    void testNextStatus_WorkInProgressToCancelled() {
        WorkflowStatus currentStatus = WorkflowStatus.WORK_IN_PROGRESS;
        assertEquals(WorkflowStatus.CANCELLED, currentStatus.nextStatus("CANCELLED"));
    }

    @Test
    void testNextStatus_NotifiedToCancelled() {
        WorkflowStatus currentStatus = WorkflowStatus.NOTIFIED;
        assertEquals(WorkflowStatus.CANCELLED, currentStatus.nextStatus("CANCELLED"));
    }

    @Test
    void testNextStatus_SignedToCancelled() {
        WorkflowStatus currentStatus = WorkflowStatus.SIGNED;
        assertEquals(WorkflowStatus.CANCELLED, currentStatus.nextStatus("CANCELLED"));
    }

    @Test
    void testNextStatus_DoneThrowsException() {
        WorkflowStatus currentStatus = WorkflowStatus.DONE;
        assertThrows(IllegalStateException.class, () -> currentStatus.nextStatus("DONE"));
    }

    @Test
    void testNextStatus_CancelledThrowsException() {
        WorkflowStatus currentStatus = WorkflowStatus.CANCELLED;
        assertThrows(IllegalStateException.class, () -> currentStatus.nextStatus("CANCELLED"));
    }
}