package com.bibik.university.mmi.entity;

public class TaskEntity {

    private final String label;
    private final int vectorIndex;

    public TaskEntity(String label, int vectorIndex) {
        this.label = label;
        this.vectorIndex = vectorIndex;
    }

    public String getLabel() {
        return label;
    }

    public int getVectorIndex() {
        return vectorIndex;
    }
}
