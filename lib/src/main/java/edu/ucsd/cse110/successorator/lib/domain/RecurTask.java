package edu.ucsd.cse110.successorator.lib.domain;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// make abstract?
public interface RecurTask {

    void addTask(Task task, LocalDateTime time);

    boolean removeTask(Task task);

    List<Task> checkRecur(LocalDateTime date);

    public List<Task> getTasksForDate(LocalDateTime time);
}
