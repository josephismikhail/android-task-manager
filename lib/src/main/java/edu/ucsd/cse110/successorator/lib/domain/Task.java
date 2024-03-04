package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class Task implements Serializable {
    private final @Nullable Integer id;
    private final @NonNull String task;
    private boolean completed;
    private final int sortOrder;
    private Long completedTime;

    public Task(@Nullable Integer id, @NonNull String task, boolean completed, int sortOrder, @Nullable Long completedTime) {
        this.id = id;
        this.task = task;
        this.completed = completed;
        this.sortOrder = sortOrder;
        this.completedTime = completedTime;
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

    public Task withId(int id) { return new Task(id, task, completed, sortOrder, completedTime); }

    public boolean isCompleted() {
        return completed;
    }

    public void changeStatus() {
        this.completed = !this.completed;
        if (this.completed) {
            this.completedTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        } else {
            this.completedTime = null;
        }
    }

    public Task withSortOrder(int sortOrder) {
        return new Task(id, task, completed, sortOrder, completedTime);
    }

    @Nullable
    public Long getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(@Nullable Long completedTime) {
        this.completedTime = completedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task1 = (Task) o;
        return completed == task1.completed && Objects.equals(id, task1.id) && Objects.equals(task, task1.task) && Objects.equals(sortOrder, task1.sortOrder) && Objects.equals(completedTime, task1.completedTime);
    }

    @Override
    public int hashCode() { return Objects.hash(id, task, completed, sortOrder, completedTime); }
}