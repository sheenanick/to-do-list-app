package com.epicodus.todolist.models;

public class Task {
    private String description;
    private boolean isComplete;

    public Task() {}

    public Task(String description) {
        this.description = description;
        this.isComplete = false;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
