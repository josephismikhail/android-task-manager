package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TaskBuilder {
    private final @Nullable Integer id;
    private final @NonNull String task;
    private boolean completed;
    private final int sortOrder;
    private Long completedTime;
//    private Context context;
    private RecurType recurType;
    private Long recurDate;
    private boolean display;

    public TaskBuilder(String task, RecurType recurType) {
        this.id = null;
        this.task = task;
        this.completed = false;
        this.sortOrder = -1;
        this.completedTime = null;
        this.recurType = recurType;
        this.recurDate = null;
        this.display = true;
    }
}
