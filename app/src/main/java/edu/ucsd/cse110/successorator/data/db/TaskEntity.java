package edu.ucsd.cse110.successorator.data.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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

    public TaskEntity(@Nullable Integer id, @NonNull String task, boolean completed, int sortOrder) {
        this.id = id;
        this.task = task;
        this.completed = completed;
        this.sortOrder = sortOrder;
    }

    public static TaskEntity fromTask(@NonNull Task task) {
        return new TaskEntity(task.id(), task.getTask(), task.isCompleted(), task.sortOrder());
    }

    public @NonNull Task toTask() {
        return new Task(id, task, completed, sortOrder);
    }

    public int getTaskID() {
        return this.id;
    }

    public String getTaskName() {
        return this.task;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public void changeStatus() {
        this.completed = !this.isCompleted();
    }

    public TaskEntity withSortOrder(int newSortOrder) {
        return new TaskEntity(this.id, this.task, this.completed, newSortOrder);
    }

    public static TaskEntity toEntity(Task task) {
        return new TaskEntity(task.id(), task.getTask(), task.isCompleted(), task.sortOrder());
    }

    public int getSortOrder() {
        return this.sortOrder;
    }
}
