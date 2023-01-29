package com.celonis.challenge.services;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.celonis.challenge.model.ExecutionState;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class TaskCleanUpJobSchedulerService {

    @Autowired
    private TaskService taskService;

    //Cron job that executes every minute
    @Scheduled(cron = "0 * * * * ?")
    public void cleanUpTasks() {
        log.info("Tasks Cleanup job started");
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        taskService.listTasks()
                .stream()
                .filter(task -> task.getExecutionState() == ExecutionState.SCHEDULED && task.getCreationDate() != null && task.getCreationDate().toInstant().isBefore(fiveMinutesAgo.atZone(ZoneId.systemDefault()).toInstant()))
                .forEach(task -> taskService.delete(task.getId()));

        log.info("Tasks Cleanup job executed successfully");

    }
}
