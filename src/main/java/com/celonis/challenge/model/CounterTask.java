package com.celonis.challenge.model;

import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class CounterTask extends ProjectGenerationTask {

    private int x;
    private int y;
}
