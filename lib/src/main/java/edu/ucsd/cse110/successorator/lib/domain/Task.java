package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Task {
    private boolean completed;
    private final @NonNull String task;
    private final @Nullable Integer id;

    public Task(@NonNull String task, @Nullable Integer id) {
       this.task = task;
       this.id = id;
       this.completed = false;
    }

    public @Nullable Integer id() {
        return id;
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
