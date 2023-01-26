
# Celonis Task Mining Application
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

Celonis Task Mining is the technology that allows businesses to capture user interaction (desktop) data, so they can analyze 
how people get work done, and how they can do it even better.

## Requirements
The requirements is to implement and enhance the existing backend REST APIs so that the webclient can monitor it.
Adding to that additionally fulfill the requirements as mentioned below :

Task 1: Dependency injection
The project you received fails to start correctly due to a problem in the dependency injection. Identify that problem and fix it.

Task 2: Extend the application
The task is to extend the current functionality of the backend by

- implementing a new task type
- showing the progress of the task execution
- implementing a task cancellation mechanism.
- The new task type is a simple counter which is configured with two input parameters, x and y of type integer. When the task is executed, counter should start in the background and progress should be monitored. Counting should start from x and get increased by one every second. When counting reaches y, the task should finish successfully.

The progress of the task should be exposed via the API so that a web client can monitor it. Canceling a task that is being executed should be possible, in which case the execution should stop.

Task 3: Periodically clean up the tasks
The API can be used to create tasks, but the user is not required to execute those tasks. The tasks that are not executed after an extended period (e.g. a week) should be periodically cleaned up (deleted).


For building and running the application you need:

- [JDK 1.11](https://www.oracle.com/java/technologies/downloads/#java11)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring application on your local machine. 

Here I am using simple one i.e. go and execute the `main` method in the `com.celonis.challenge.TaskMiningApplication` class from your IDE.

Note: As we are using IDE, it will automatically build when you run the application or else use this command `mvn clean install` to build application

After execution completed, you can download the postman collection attached [HERE](postman-collection/task_mining.postman_collection.json) and test all the APIs locally.

## Copyright
Celonis information on What is Task Mining (https://www.celonis.com/process-mining/what-is-task-mining/)

Released under the Apache License 2.0. See the [LICENSE](https://www.apache.org/licenses/LICENSE-2.0) file.
