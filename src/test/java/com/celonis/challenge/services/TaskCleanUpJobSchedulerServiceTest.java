package com.celonis.challenge.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.celonis.challenge.model.ExecutionState;
import com.celonis.challenge.model.ProjectGenerationTask;

@ExtendWith(MockitoExtension.class)
class TaskCleanUpJobSchedulerServiceTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskCleanUpJobSchedulerService taskCleanUpJobSchedulerService;

    @Test
    void testCleanUpTasks() {

        List<ProjectGenerationTask> taskList = new ArrayList<>();
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        ProjectGenerationTask task1 = new ProjectGenerationTask();
        task1.setCreationDate(Date.from(fiveMinutesAgo.atZone(ZoneId.systemDefault()).toInstant()));
        task1.setExecutionState(ExecutionState.SCHEDULED);
        task1.setId("1");

        ProjectGenerationTask task2 = new ProjectGenerationTask();
        task2.setCreationDate(Date.from(fiveMinutesAgo.atZone(ZoneId.systemDefault()).toInstant()));
        task2.setExecutionState(ExecutionState.RUNNING);
        task2.setId("2");

        taskList.add(task1);
        taskList.add(task2);

        when(taskService.listTasks()).thenReturn(taskList);

        taskCleanUpJobSchedulerService.cleanUpTasks();

        verify(taskService).listTasks();
        verify(taskService).delete("1");
        verifyNoMoreInteractions(taskService);
    }

    @Test
    public void testCleanUpTasks_Failure() {

        when(taskService.listTasks()).thenReturn(null);
        taskCleanUpJobSchedulerService.cleanUpTasks();
        verify(taskService, times(0)).delete(anyString());
        verify(taskService, times(1)).listTasks();
    }
}
