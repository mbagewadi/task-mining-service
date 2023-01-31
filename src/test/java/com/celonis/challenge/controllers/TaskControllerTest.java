package com.celonis.challenge.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.celonis.challenge.model.CounterTask;
import com.celonis.challenge.model.ExecutionState;
import com.celonis.challenge.model.ProjectGenerationTask;
import com.celonis.challenge.services.FileService;
import com.celonis.challenge.services.TaskService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private FileService fileService;

    @Test
    void listTasks_ShouldReturnListOfTasks() throws Exception {
        var task1 = new ProjectGenerationTask();
        task1.setId("1");
        task1.setName("Test Task 1");
        var task2 = new ProjectGenerationTask();
        task2.setId("2");
        task2.setName("Test Task 2");
        when(taskService.listTasks()).thenReturn(Arrays.asList(task1, task2));

        mvc.perform(get("/api/tasks/"))
                .andExpect(status().isOk());
    }

    @Test
    void createTask_ShouldReturnCreatedTask() throws Exception {
        var task = new ProjectGenerationTask();
        task.setId("1");
        task.setName("Test Task 1");
        when(taskService.createTask(task)).thenReturn(task);

        mvc.perform(post("/api/tasks/")
                .contentType("application/json")
                .content("{\"taskId\":\"1\",\"taskName\":\"Test Task\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void getTask_ShouldReturnTask() throws Exception {
        var task = new ProjectGenerationTask();
        task.setId("1");
        task.setName("Test Task 1");
        when(taskService.getTask("1")).thenReturn(task);

        mvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk());
    }

    @Test
    void updateTask_ShouldReturnUpdatedTask() throws Exception {
        var task = new ProjectGenerationTask();
        task.setId("1");
        task.setName("Test Task 1");
        when(taskService.update("1", task)).thenReturn(task);
        mvc.perform(put("/api/tasks/1")
                .contentType("application/json")
                .content("{\"taskId\":\"1\",\"taskName\":\"Test Task\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTask_ShouldReturnNoContent() throws Exception {
        mvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void executeTask_ShouldReturnNoContent() throws Exception {
        mvc.perform(post("/api/tasks/1/execute"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getResult_ShouldReturnResultFile() throws Exception {
        var file = new FileSystemResource("file.txt");
        when(fileService.getTaskResult("1")).thenReturn(ResponseEntity.ok(file));
        mvc.perform(get("/api/tasks/1/result"))
                .andExpect(status().isOk());
    }

    @Test
    void createCounterTask_ShouldReturnCreatedCounterTask() throws Exception {
        var counterTask = new CounterTask();
        counterTask.setId("1");
        counterTask.setName("Test Counter Task");
        counterTask.setExecutionState(ExecutionState.SCHEDULED);
        counterTask.setX(10);
        counterTask.setY(20);
        when(taskService.createTask(counterTask)).thenReturn(counterTask);
        mvc.perform(post("/api/tasks/counter")
                .contentType("application/json")
                .content("{\"taskId\":\"1\",\"taskName\":\"Test Counter Task\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void getStateOfTaskExecution_ShouldReturnTaskExecutionState() throws Exception {
        var counterTask = new CounterTask();
        counterTask.setId("1");
        counterTask.setExecutionState(ExecutionState.RUNNING);
        when(taskService.getStateOfTaskExecution("1")).thenReturn(ExecutionState.RUNNING);
        mvc.perform(get("/api/tasks/1/progress"))
                .andExpect(status().isOk());
    }

    @Test
    void cancelTaskExecution_ShouldReturnNoContent() throws Exception {
        mvc.perform(post("/api/tasks/1/cancel"))
                .andExpect(status().isNoContent());
    }

}
