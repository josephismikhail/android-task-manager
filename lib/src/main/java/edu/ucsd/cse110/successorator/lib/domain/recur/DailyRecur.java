package edu.ucsd.cse110.successorator.lib.domain.recur;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.successorator.lib.domain.Task;

public class DailyRecur extends RecurTask {
    DayOfWeek currentDay;
    private final Map<Task, LocalDateTime> recurringTasks;

    public DailyRecur(DayOfWeek currentDay) {
        this.currentDay = currentDay;
        this.recurringTasks = new HashMap<>();
    }

    @Override
    public void addTask(Task task, LocalDateTime date) {
        recurringTasks.put(task, null);
    }

    @Override
    public boolean removeTask(Task task) {
        return recurringTasks.remove(task, recurringTasks.get(task));
    }

    @Override
    public List<Task> checkRecur(LocalDateTime time) {
        if (time.getDayOfWeek() != currentDay) {
            this.currentDay = time.getDayOfWeek();
            return new ArrayList<>(recurringTasks.keySet());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Task> getTasksForDate(LocalDateTime time) {
        return new ArrayList<>(recurringTasks.keySet());
    }
}
