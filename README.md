
# Celonis Task Mining Application
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

Celonis Task Mining is a technology that captures desktop user interaction data to analyze work processes and improve them.

## Requirements
The goal is to improve the existing backend REST APIs and add the following features:

Task 1: Dependency Injection Fix
* Identify and resolve a problem with the dependency injection to get the project to start correctly.

Task 2: Extend Application Functionality
* Implement a new task type: a counter task with two input parameters, x and y of type integer.
* Monitor task progress via API.
* Allow for task cancellation.
* The counter task starts counting from x and increments by 1 every second, ending when it reaches y.

Task 3: Periodic Task Cleanup
* Automatically delete tasks that are not executed and older than a week(Note: Just for demo , added logic older than 5minutes).

##Prerequisites
- [JDK 1.11](https://www.oracle.com/java/technologies/downloads/#java11)
- [Maven 3](https://maven.apache.org)

## Running the Application Locally

* Run the `main` method in
  `com.celonis.challenge.TaskMiningApplication` class using an IDE.
* Alternatively, `run mvn clean install` to build the application.
* Test the APIs with the attached Postman collection [HERE](postman-collection/task-mining-apis.postman_collection.json)

## Copyright

More information on Celonis Task Mining at https://www.celonis.com/process-mining/what-is-task-mining/

Released under Apache License 2.0. See the [LICENSE](https://www.apache.org/licenses/LICENSE-2.0) file.
