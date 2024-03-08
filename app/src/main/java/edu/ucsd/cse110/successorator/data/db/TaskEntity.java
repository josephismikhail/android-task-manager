package edu.ucsd.cse110.successorator.data.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import edu.ucsd.cse110.successorator.lib.domain.RecurType;
import edu.ucsd.cse110.successorator.lib.domain.Task;
@Entity(tableName = "tasks")
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id;

    @ColumnInfo(name = "task")
    public String task;

    @ColumnInfo(name = "completed")
    public boolean completed;

    @ColumnInfo(name = "sortOrder")
    public int sortOrder;

    @ColumnInfo(name = "completedTime")
    public Long completedTime;

    @ColumnInfo(name = "recurType")
    public RecurType recurType;

    @ColumnInfo(name = "recurDate")
    public Long recurDate;

    @ColumnInfo(name = "display")
    public boolean display;

    TaskEntity(@Nullable Integer id, @NonNull String task, boolean completed, int sortOrder,
               @Nullable Long completedTime, RecurType recurType, Long recurDate, boolean display) {
        this.id = id;
        this.task = task;
        this.completed = completed;
        this.sortOrder = sortOrder;
        this.completedTime = completedTime;
        this.recurType = recurType;
        this.recurDate = recurDate;
        this.display = display;
    }

    public static TaskEntity fromTask(@NonNull Task task) {
        return new TaskEntity(task.getId(), task.getTask(), task.isCompleted(), task.getSortOrder(),
                task.getCompletedTime(), task.getRecurType(), task.getRecurDate(), task.display());
    }

    public @NonNull Task toTask() {
        return new Task(id, task, completed, sortOrder, completedTime, recurType, recurDate, display);
    }

    public int getTaskID() {
        return this.id;
    }

    public String getTask() {
        return this.task;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public int getSortOrder() {
        return this.sortOrder;
    }

    public Long getCompletedTime() {
        return completedTime;
    }

    public RecurType getRecurType() {
        return recurType;
    }

    public Long getRecurDate() {
        return recurDate;
    }

    public void changeStatus() {
        this.completed = !this.isCompleted();
        if (this.completed) {
            this.completedTime = LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(-8));//date.toEpochSecond(ZoneOffset.ofHours(-8));//.toInstant().toEpochMilli();
        } else {
            this.completedTime = null;
        }
    }

    public void uncomplete() {
        this.completed = false;
    }

    public TaskEntity withSortOrder(int newSortOrder) {
        return new TaskEntity(this.id, this.task, this.completed, newSortOrder,
                this.completedTime, this.recurType, this.recurDate, this.display);
    }

    public void setDisplay(boolean b) {
        this.display = b;
    }
}
