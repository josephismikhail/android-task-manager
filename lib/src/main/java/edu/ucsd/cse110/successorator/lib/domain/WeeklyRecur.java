package edu.ucsd.cse110.successorator.lib.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class WeeklyRecur implements RecurTask {
    private List<LocalDateTime> dateRecurred;
    private List<Task> recurringTasks;

    public WeeklyRecur() {
        this.dateRecurred = new ArrayList<>();
        this.recurringTasks = new ArrayList<>();
    }

    @Override
    public void addTask(Task task, LocalDateTime date) {
        recurringTasks.add(task);
        dateRecurred.add(date);
    }

    @Override
    public boolean removeTask(Task task) {
        int index = recurringTasks.indexOf(task);
        dateRecurred.remove(index);
        return recurringTasks.remove(task);
    }

    @Override
    public List<Task> checkRecur(LocalDateTime date) {
        List<Task> recurTasks = new ArrayList<>();
        for (int i = 0; i < dateRecurred.size(); i++) {
            var taskDate = dateRecurred.get(i);
            var daysUntil = taskDate.until(date, ChronoUnit.DAYS);
            if (daysUntil > 1) {
                if (taskDate.getDayOfWeek() == date.getDayOfWeek()) {
                    recurTasks.add(recurringTasks.get(i));
                    dateRecurred.set(i, date);
                }
            }
        }
        return recurTasks;
    }
}
