package com.celonis.challenge.controllers;

import com.celonis.challenge.model.CounterTask;
import com.celonis.challenge.model.ExecutionState;
import com.celonis.challenge.model.ProjectGenerationTask;
import com.celonis.challenge.services.FileService;
import com.celonis.challenge.services.TaskService;
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
    public ProjectGenerationTask createTask(@RequestBody @Valid ProjectGenerationTask projectGenerationTask) {
        return taskService.createTask(projectGenerationTask);
    }

    @GetMapping("/{taskId}")
    public ProjectGenerationTask getTask(@PathVariable String taskId) {
        return taskService.getTask(taskId);
    }

    @RequestMapping(value = "/{taskId}", method = RequestMethod.PUT)
    public ProjectGenerationTask updateTask(@PathVariable String taskId,
                                            @RequestBody @Valid ProjectGenerationTask projectGenerationTask) {
        return taskService.update(taskId, projectGenerationTask);
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable String taskId) {
        taskService.delete(taskId);
    }

    @PostMapping("/{taskId}/execute")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void executeTask(@PathVariable String taskId) {
        taskService.executeTask(taskId);
    }

    @GetMapping("/{taskId}/result")
    public ResponseEntity<FileSystemResource> getResult(@PathVariable String taskId) {
        return fileService.getTaskResult(taskId);
    }

    @PostMapping("/counter")
    public CounterTask createCounterTask(@RequestBody @Valid CounterTask counterTask) {
        return (CounterTask) taskService.createTask(counterTask);
    }

    @GetMapping("/{taskId}/progress")
    public ExecutionState getStateOfTaskExecution(@PathVariable String taskId) {
        return taskService.getStateOfTaskExecution(taskId);
    }

    @PostMapping("/{taskId}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelTaskExecution(@PathVariable String taskId) {
        taskService.cancelTaskExecution(taskId);
    }

}
