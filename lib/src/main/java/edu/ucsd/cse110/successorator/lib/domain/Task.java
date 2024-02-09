package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;

public class Task {
    private boolean completed;
    private final @NonNull String task;

    public Task(@NonNull String task) {
       this.task = task;
       this.completed = false;
    }

    public String getTask() {
        return this.task;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public void completeTask() {
       this.completed = true;
    }

    public void uncompleteTask() {
        this.completed = false;
    }
}
