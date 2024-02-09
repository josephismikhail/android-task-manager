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

    TaskEntity(@NonNull String task, @Nullable Integer id) {
        this.completed = false;
        this.task = task;
        this.id = id;
    }

    public static TaskEntity fromTask(@NonNull Task task) {
        var t = new TaskEntity(task.getTask(), task.id());
        return t;
    }

    public @NonNull Task toTask() {
        return new Task(task, id);
    }
}
