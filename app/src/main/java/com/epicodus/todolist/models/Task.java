package com.epicodus.todolist.models;

public class Task {
    private String description;
    private boolean isComplete;
    private String pushId;
    private String index;

    public Task() {}

    public Task(String description) {
        this.description = description;
        this.isComplete = false;
        this.index = "not_specified";
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

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
