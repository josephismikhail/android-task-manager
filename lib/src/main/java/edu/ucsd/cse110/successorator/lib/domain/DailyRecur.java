package edu.ucsd.cse110.successorator.lib.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DailyRecur implements RecurTask {
    DayOfWeek currentDay;
    private List<Task> recurringTasks;

    public DailyRecur(DayOfWeek currentDay) {
        this.currentDay = currentDay;
        this.recurringTasks = new ArrayList<>();
    }

    @Override
    public void addTask(Task task, LocalDateTime date) {
        recurringTasks.add(task);
    }

    @Override
    public boolean removeTask(Task task) {
        return recurringTasks.remove(task);
    }

    @Override
    public List<Task> checkRecur(LocalDateTime time) {
        if (time.getDayOfWeek() != currentDay) {
            this.currentDay = time.getDayOfWeek();
            return recurringTasks;
        } else {
            return new ArrayList<Task>();
        }
    }
}
