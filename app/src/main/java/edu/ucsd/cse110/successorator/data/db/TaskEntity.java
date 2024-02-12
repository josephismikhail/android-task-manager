package edu.ucsd.cse110.successorator.data.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

import edu.ucsd.cse110.successorator.lib.domain.Task;
@Entity(tableName = "tasks")
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "completed")
    public boolean completed;

    @ColumnInfo(name = "task")
    public String task;

    @ColumnInfo(name = "id")
    public Integer id;

    @ColumnInfo(name = "sortOrder")
    public int sortOrder;

    TaskEntity(@NonNull String task, @Nullable Integer id, int sortOrder) {
        this.task = task;
        this.id = id;
        this.sortOrder = sortOrder;
        this.completed = false;
    }

    public static TaskEntity fromTask(@NonNull Task task) {
        return new TaskEntity(task.getTask(), task.id(), task.sortOrder());
    }

    public @NonNull Task toTask() {
        return new Task(task, id, sortOrder);
    }
}
