package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;

public class Task implements Serializable {
    private final @Nullable Integer id;
    private final @NonNull String task;
    private boolean completed;
    private int sortOrder;

    public Task(@Nullable Integer id, @NonNull String task, boolean completed, int sortOrder) {
        this.id = id;
        this.task = task;
        this.completed = completed;
        this.sortOrder = sortOrder;
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

    public boolean isCompleted() {
        return completed;
    }

    public void changeStatus() {
        this.completed = !this.completed;
    }

    public Task withId(int id) { return new Task(id, task, completed, sortOrder); }

    public Task withSortOrder(int sortOrder) {
        return new Task(id, task, completed, sortOrder);
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

    public void setCompleted(boolean b) {
        this.completed = b;
    }
}