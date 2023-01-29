package com.celonis.challenge.services;

import com.celonis.challenge.exceptions.InternalException;
import com.celonis.challenge.exceptions.NotFoundException;
import com.celonis.challenge.model.CounterTask;
import com.celonis.challenge.model.ExecutionState;
import com.celonis.challenge.model.ProjectGenerationTask;
import com.celonis.challenge.model.ProjectGenerationTaskRepository;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
@Log4j2
public class TaskService {

    @Autowired
    private ProjectGenerationTaskRepository projectGenerationTaskRepository;

    @Autowired
    private FileService fileService;

    public List<ProjectGenerationTask> listTasks() {
        return projectGenerationTaskRepository.findAll();
    }

    public ProjectGenerationTask createTask(ProjectGenerationTask projectGenerationTask) {
        projectGenerationTask.setId(null);
        projectGenerationTask.setCreationDate(new Date());
        return projectGenerationTaskRepository.save(projectGenerationTask);
    }

    public ProjectGenerationTask getTask(String taskId) {
        return get(taskId);
    }

    public ProjectGenerationTask update(String taskId, ProjectGenerationTask projectGenerationTask) {
        ProjectGenerationTask existing = get(taskId);
        existing.setCreationDate(projectGenerationTask.getCreationDate());
        existing.setName(projectGenerationTask.getName());
        return projectGenerationTaskRepository.save(existing);
    }

    public void delete(String taskId) {
        projectGenerationTaskRepository.deleteById(taskId);
    }

    public CompletableFuture<Void> executeTask(String taskId) {
        ProjectGenerationTask task = get(taskId);
        if (task instanceof CounterTask) {
            CounterTask counterTask = (CounterTask) task;
            if (counterTask.getExecutionState() == ExecutionState.CANCELLED) {
                return CompletableFuture.failedFuture(new InternalException("Task is already in CANCELLED state"));
            }
            return CompletableFuture.runAsync(() -> count(counterTask));
        } else {
            URL url = Thread.currentThread().getContextClassLoader().getResource("challenge.zip");
            if (url == null) {
                return CompletableFuture.failedFuture(new InternalException("Zip file not found"));
            }
            try {
                return CompletableFuture.supplyAsync(() -> {
                    try {
                        fileService.storeResult(taskId, url);
                        task.setExecutionState(ExecutionState.COMPLETED);
                        projectGenerationTaskRepository.save(task);
                        return null;
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }
                });
            } catch (Exception e) {
                return CompletableFuture.failedFuture(new InternalException(e));
            }
        }
    }

    private void count(CounterTask counterTask) {
        for (int i = counterTask.getX(); i <= counterTask.getY(); i++) {
            counterTask.setExecutionState(ExecutionState.RUNNING);
            projectGenerationTaskRepository.save(counterTask);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("Error occured during execution");
            }
        }
        counterTask.setExecutionState(ExecutionState.COMPLETED);
        projectGenerationTaskRepository.save(counterTask);
    }

    public ExecutionState getStateOfTaskExecution(String taskId) {
        CounterTask counterTask = (CounterTask) get(taskId);
        return counterTask.getExecutionState();
    }

    public void cancelTaskExecution(String taskId) {
        CounterTask counterTask = (CounterTask) get(taskId);
        counterTask.setExecutionState(ExecutionState.CANCELLED);
        projectGenerationTaskRepository.save(counterTask);
    }

    private ProjectGenerationTask get(String taskId) {
        Optional<ProjectGenerationTask> projectGenerationTask = projectGenerationTaskRepository.findById(taskId);
        return projectGenerationTask.orElseThrow(NotFoundException::new);
    }
}
