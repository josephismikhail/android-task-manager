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

    @ColumnInfo(name = "completedTime")
    public Long completedTime;

    TaskEntity(@Nullable Integer id, @NonNull String task, boolean completed, int sortOrder, @Nullable Long completedTime) {
        this.id = id;
        this.task = task;
        this.completed = completed;
        this.sortOrder = sortOrder;
        this.completedTime = completedTime;
    }

    public static TaskEntity fromTask(@NonNull Task task) {
        return new TaskEntity(task.id(), task.getTask(), task.isCompleted(), task.sortOrder(), task.getCompletedTime());
    }

    public @NonNull Task toTask() {
        return new Task(id, task, completed, sortOrder, completedTime);
    }
}
