package com.celonis.challenge.controllers;

import com.celonis.challenge.model.CounterTask;
import com.celonis.challenge.model.ExecutionState;
import com.celonis.challenge.model.ProjectGenerationTask;
import com.celonis.challenge.services.FileService;
import com.celonis.challenge.services.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    private final FileService fileService;

    @Autowired
    public TaskController(TaskService taskService,
                          FileService fileService) {
        this.taskService = taskService;
        this.fileService = fileService;
    }

    @GetMapping("/")
    public List<ProjectGenerationTask> listTasks() {
        return taskService.listTasks();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ProjectGenerationTask> createTask(@RequestBody @Valid ProjectGenerationTask projectGenerationTask) {
        ProjectGenerationTask createdTask = taskService.createTask(projectGenerationTask);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ProjectGenerationTask> getTask(@PathVariable String taskId) {
        ProjectGenerationTask task = taskService.getTask(taskId);
        return ResponseEntity.ok(task);
    }

    @RequestMapping(value = "/{taskId}", method = RequestMethod.PUT)
    public ResponseEntity<ProjectGenerationTask> updateTask(@PathVariable String taskId,
                                            @RequestBody @Valid ProjectGenerationTask projectGenerationTask) {
        ProjectGenerationTask updatedTask = taskService.update(taskId, projectGenerationTask);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteTask(@PathVariable String taskId) {
        taskService.delete(taskId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{taskId}/execute")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> executeTask(@PathVariable String taskId) {
        taskService.executeTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{taskId}/result")
    public ResponseEntity<FileSystemResource> getResult(@PathVariable String taskId) {
        return fileService.getTaskResult(taskId);
    }

    @PostMapping("/counter")
    public ResponseEntity<CounterTask> createCounterTask(@RequestBody @Valid CounterTask counterTask) {
        CounterTask createdTask = (CounterTask) taskService.createTask(counterTask);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping("/{taskId}/progress")
    public ResponseEntity<ExecutionState> getStateOfTaskExecution(@PathVariable String taskId) {
        ExecutionState executionState = taskService.getStateOfTaskExecution(taskId);
        return ResponseEntity.ok(executionState);
    }

    @PostMapping("/{taskId}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> cancelTaskExecution(@PathVariable String taskId) {
        taskService.cancelTaskExecution(taskId);
        return ResponseEntity.noContent().build();
    }

}
