package com.celonis.challenge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class ProjectGenerationTask {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String name;

    private Date creationDate;

    @JsonIgnore
    private String storageLocation;

    private ExecutionState executionState = ExecutionState.SCHEDULED;

}
