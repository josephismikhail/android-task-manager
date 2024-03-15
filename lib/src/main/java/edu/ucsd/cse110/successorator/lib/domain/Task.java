package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

public class Task implements Serializable {
    private final @Nullable Integer id;
    private final @NonNull String task;
    private boolean completed;
    private final int sortOrder;
    private Long completedTime;
    private TaskContext context;
    private RecurType recurType;
    private Long recurDate;
    private boolean display;

    public Task(@Nullable Integer id, @NonNull String task, boolean completed, int sortOrder,
                @Nullable Long completedTime, TaskContext context, RecurType recurType, Long recurDate, boolean display) {
        this.id = id;
        this.task = task;
        this.completed = completed;
        this.sortOrder = sortOrder;
        this.completedTime = completedTime;
        this.context = context;
        this.recurType = recurType;
        this.recurDate = recurDate;
        this.display = display;
    }

    @Nullable
    public Integer getId() {
        return id;
    }

    @NonNull
    public String getTask() {
        return this.task;
    }

    public boolean isCompleted() {
        return completed;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    @Nullable
    public Long getCompletedTime() {
        return completedTime;
    }

    public TaskContext getContext() {
        return context;
    }

    public RecurType getRecurType() {
        return recurType;
    }

    public Long getRecurDate() {
        return recurDate;
    }

    public boolean display() {
        return display;
    }

    public Task withId(int id) {
        return new Task(id, task, completed, sortOrder, completedTime, context, recurType, recurDate, display);
    }

    public void changeStatus() {
        this.completed = !this.completed;
        if (this.completed) {
            this.completedTime = LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(-8));//.toInstant().toEpochMilli();
        } else {
            this.completedTime = null;
        }
    }

    public void uncomplete() {
        this.completed = false;
    }

    public Task withSortOrder(int sortOrder) {
        return new Task(id, task, completed, sortOrder, completedTime, context, recurType, recurDate, display);
    }

    public Task withRecurType(RecurType recurType) {
        return new Task(id, task, completed, sortOrder, completedTime, context, recurType, recurDate, display);
    }

    public Task withRecurDate(Long recurDate) {
        return new Task(id, task, completed, sortOrder, completedTime, context, recurType, recurDate, display);
    }

    public void setDisplay(boolean display) {
        this.display = display;
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