package com.akashungarala.task_manager;

import java.io.Serializable;

public class Task implements Serializable {
    private String name, due_date, notes, priority_level, status;
    public Task() {}
    public Task(String name, String priority_level) {
        this.name = name;
        this.priority_level = priority_level;
    }
    public Task(String name, String due_date, String notes, String priority_level, String status) {
        this.name = name;
        this.due_date = due_date;
        this.notes = notes;
        this.priority_level = priority_level;
        this.status = status;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDue_date() {
        return due_date;
    }
    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public String getPriority_level() {
        return priority_level;
    }
    public void setPriority_level(String priority_level) {
        this.priority_level = priority_level;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}