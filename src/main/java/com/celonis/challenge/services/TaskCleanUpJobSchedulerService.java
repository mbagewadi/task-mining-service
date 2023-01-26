package com.celonis.challenge.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class TaskCleanUpJobSchedulerService {

    @Autowired
    private TaskService taskService;

    //Cron job that executes every 5 minutes
    @Scheduled(cron = "0 */5 * * * ?")
    public void cleanUpTasks() {
        log.info("Tasks Cleanup job started");
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        taskService.listTasks()
                .stream()
                .filter(task -> task.getCreationDate() != null && task.getCreationDate().before(Date.from(sevenDaysAgo.atStartOfDay(ZoneId.systemDefault()).toInstant())))
                .forEach(task -> taskService.delete(task.getId()));

        log.info("Tasks Cleanup job executed successfully");

    }
}
