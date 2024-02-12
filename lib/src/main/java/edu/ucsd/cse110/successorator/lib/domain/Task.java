package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;

public class Task implements Serializable {
    private final @NonNull String task;
    private final @Nullable Integer id;
    private final int sortOrder;
    private boolean completed;

    public Task(@NonNull String task, @Nullable Integer id, int sortOrder) {
        this.task = task;
        this.id = id;
        this.sortOrder = sortOrder;
        this.completed = false;
    }

    @Nullable
    public Integer id() {
        return id;
    }

    @NonNull
    public String getTask() {
        return this.task;
    }

    public int sortOrder() {
        return sortOrder;
    }

    public Task withId(int id) { return new Task(task, id, sortOrder); }

    public boolean isCompleted() {
        return this.completed;
    }

    public void completeTask() {
       this.completed = true;
    }

    public void uncompleteTask() {
        this.completed = false;
    }

    public Task withSortOrder(int sortOrder) {
        return new Task(task, id, sortOrder);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task1 = (Task) o;
        return completed == task1.completed && Objects.equals(id, task1.id) && Objects.equals(task, task1.task) && Objects.equals(sortOrder, task1.sortOrder);
    }

    @Override
    public int hashCode() { return Objects.hash(completed, id, task, sortOrder); }
}