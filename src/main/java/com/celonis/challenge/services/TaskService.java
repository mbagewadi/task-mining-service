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
        if(projectGenerationTask == null) {
            throw new InternalException("BadRequest: Task is null");
        }
        projectGenerationTask.setId(null);
        projectGenerationTask.setCreationDate(new Date());
        return projectGenerationTaskRepository.save(projectGenerationTask);
    }

    public ProjectGenerationTask getTask(String taskId) {
        validateTaskId(taskId);
        return get(taskId);
    }

    public ProjectGenerationTask update(String taskId, ProjectGenerationTask projectGenerationTask) {
        if (taskId == null || taskId.isEmpty() || projectGenerationTask == null) {
            throw new InternalException("Task Id or Task is null or empty");
        }
        ProjectGenerationTask existing = get(taskId);
        //existing.setCreationDate(projectGenerationTask.getCreationDate());
        existing.setName(projectGenerationTask.getName());
        return projectGenerationTaskRepository.save(existing);
    }

    public void delete(String taskId) {
        validateTaskId(taskId);
        projectGenerationTaskRepository.deleteById(taskId);
    }

    public void executeTask(String taskId) {
        validateTaskId(taskId);
        ProjectGenerationTask task = get(taskId);
        if(task instanceof CounterTask){
            executeCounterTask((CounterTask) task);
        } else{
            executeOtherTask(taskId);
            task.setExecutionState(ExecutionState.COMPLETED);
            projectGenerationTaskRepository.save(task);
        }
    }

    private void executeOtherTask(String taskId) {

        URL url = Thread.currentThread().getContextClassLoader().getResource("challenge.zip");
        if (url == null) {
            throw new InternalException("Zip file not found");
        }
        try {
            fileService.storeResult(taskId, url);

        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    private CompletableFuture<Void> executeCounterTask(CounterTask counterTask) {

            if (counterTask.getExecutionState() == ExecutionState.CANCELLED) {
                return CompletableFuture.failedFuture(new InternalException("Task is already in CANCELLED state"));
            }
            return CompletableFuture.runAsync(() -> count(counterTask));

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
        validateTaskId(taskId);
        CounterTask counterTask = (CounterTask) get(taskId);
        return counterTask.getExecutionState();
    }

    public void cancelTaskExecution(String taskId) {
        validateTaskId(taskId);
        CounterTask counterTask =  (CounterTask) get(taskId);
        counterTask.setExecutionState(ExecutionState.CANCELLED);
        projectGenerationTaskRepository.save(counterTask);
    }

    private void validateTaskId(String taskId) {
        if (taskId == null || taskId.isEmpty()) {
            throw new InternalException("Task Id is null or empty");
        }
    }

    private ProjectGenerationTask get(String taskId) {
        Optional<ProjectGenerationTask> projectGenerationTask = projectGenerationTaskRepository.findById(taskId);
        return projectGenerationTask.orElseThrow(NotFoundException::new);
    }

}
