package edu.ucsd.cse110.successorator.lib.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YearlyRecur extends RecurTask {
    private final Map<Task, LocalDateTime> recurringTasks;

    public YearlyRecur() {
        this.recurringTasks = new HashMap<>();
    }

    @Override
    public void addTask(Task task, LocalDateTime date) {
        recurringTasks.put(task, date);
    }

    @Override
    public boolean removeTask(Task task) {
        return recurringTasks.remove(task, recurringTasks.get(task));
    }

    // if recur on 29th of february (leap year), the task must recur on march 1st
    @Override
    public List<Task> checkRecur(LocalDateTime date) {
        List<Task> recurTasks = new ArrayList<>(); // List of tasks to be recurred
        for (Map.Entry<Task, LocalDateTime> entry : recurringTasks.entrySet()) {
            var task = entry.getKey();
            var taskDate = entry.getValue(); // Last date task was recurred

            if (taskDate.until(date, ChronoUnit.DAYS) > 1) { // Checks if at least a day passed
                if (taskDate.getDayOfYear() == date.getDayOfYear()) {
                    recurTasks.add(task);
                    recurringTasks.replace(task, date);
                }
            }
        }
        return recurTasks;
    }

    @Override
    public List<Task> getTasksForDate(LocalDateTime time) {
        return new ArrayList<>(recurringTasks.keySet());
    }
}
