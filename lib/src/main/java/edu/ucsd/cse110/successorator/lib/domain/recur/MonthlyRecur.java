package edu.ucsd.cse110.successorator.lib.domain.recur;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.successorator.lib.domain.Task;

public class MonthlyRecur extends RecurTask {
    private final Map<Task, LocalDateTime> recurringTasks;

    public MonthlyRecur() {
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

    // Monthly recurring has to be recurring on the X monday of a month, for example,
    @Override
    public List<Task> checkRecur(LocalDateTime date) {
        List<Task> recurTasks = new ArrayList<>(); // List of tasks to be recurred
        for (Map.Entry<Task, LocalDateTime> entry : recurringTasks.entrySet()) {
            var task = entry.getKey();
            var taskDate = entry.getValue(); // Last date task was recurred

            if (taskDate.until(date, ChronoUnit.DAYS) > 1) { // Checks if at least a day passed
                if (taskDate.getDayOfMonth() == date.getDayOfMonth()) {
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
