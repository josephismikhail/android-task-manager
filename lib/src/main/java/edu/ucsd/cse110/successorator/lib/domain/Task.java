package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

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

    public @NonNull String getTask() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task1 = (Task) o;
        return completed == task1.completed && Objects.equals(task, task1.task) && Objects.equals(id, task1.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(completed, task, id);
    }
}