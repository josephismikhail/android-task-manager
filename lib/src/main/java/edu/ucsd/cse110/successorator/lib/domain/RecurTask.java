package edu.ucsd.cse110.successorator.lib.domain;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class RecurTask {

    abstract void addTask(Task task, LocalDateTime time);

    abstract boolean removeTask(Task task);

    abstract List<Task> checkRecur(LocalDateTime date);

    abstract public List<Task> getTasksForDate(LocalDateTime time);
}
