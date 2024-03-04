package edu.ucsd.cse110.successorator.lib.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class YearlyRecur implements RecurTask {
    private List<LocalDateTime> dateRecurred;
    private List<Task> recurringTasks;

    public YearlyRecur() {
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
        List<Task> recurTasks = new ArrayList<>(); // List of tasks to be recurred
        for (int i = 0; i < dateRecurred.size(); i++) {
            var taskDate = dateRecurred.get(i); // Last date task was recurred
            if (taskDate.until(date, ChronoUnit.DAYS) > 1) { // Checks if at least a day passed
                if (taskDate.getDayOfYear() == date.getDayOfYear()) {
                    recurTasks.add(recurringTasks.get(i));
                    dateRecurred.set(i, date);
                }
            }
        }
        return recurTasks;
    }
}
