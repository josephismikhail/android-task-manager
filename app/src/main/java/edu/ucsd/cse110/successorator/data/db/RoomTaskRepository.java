package edu.ucsd.cse110.successorator.data.db;

import static edu.ucsd.cse110.successorator.data.db.TaskEntity.fromTask;

import androidx.lifecycle.Transformations;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.RecurType;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.util.LiveDataSubjectAdapter;

public class RoomTaskRepository implements TaskRepository {
    private final TaskDao taskDao;

    public RoomTaskRepository(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public Subject<Task> find(int id) {
        var entityLiveData = taskDao.findAsLiveData(id);
        var taskLiveData = Transformations.map(entityLiveData,TaskEntity::toTask);
        return new LiveDataSubjectAdapter<>(taskLiveData);
    }

    @Override
    public Subject<List<Task>> findAll() {
        var entitiesLiveData = taskDao.findAllAsLiveData();
        var tasksLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(TaskEntity::toTask)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(tasksLiveData);
    }

    @Override
    public void save(Task task) {
        taskDao.insert(fromTask(task));
    }

    @Override
    public void save(List<Task> taskList) {
        var entities = taskList.stream()
                .map(TaskEntity::fromTask)
                .collect(Collectors.toList());
        taskDao.insert(entities);
    }

    @Override
    public void newTask(Task task) {
        int maxSortOrder = taskDao.getMaxSortOrder(); // Implement this to fetch the maximum sortOrder among all tasks.
        int maxIncompleteSortOrder = taskDao.getIncompleteMaxSortOrder(); // Implement this to fetch the maximum sortOrder among incomplete tasks.

        if (maxIncompleteSortOrder < maxSortOrder) { // There are completed tasks.
            taskDao.shiftSortOrder(maxIncompleteSortOrder + 1, maxSortOrder, 1); // shift completed tasks by 1
            task = task.withSortOrder(maxIncompleteSortOrder + 1);
        }
        else { // no completed tasks
            task = task.withSortOrder(maxSortOrder + 1);
        }
        // Save the updated task
        taskDao.insert(TaskEntity.fromTask(task));
    }

    @Override
    public void updateDisplayTask(LocalDateTime date) {
        List<TaskEntity> taskList = taskDao.findAll();
        if (taskList == null) return;

        for (TaskEntity task : taskList) {
            if (!checkRecurTask(task, date) && task.isCompleted()
                    && completedBeforeToday(task.toTask(), date)) {
                task.setDisplay(false);
                taskDao.insert(task);
            } else if (checkRecurTask(task, date) && task.isCompleted()) {
                task.changeStatus();
                task.setDisplay(true);
                taskDao.insert(task);
            } else if (checkRecurTask(task, date) && !task.isCompleted()) {
                task.setDisplay(true);
                taskDao.insert(task);
            } else if (!task.isCompleted() && task.recurType == RecurType.ONCE) {
                task.setDisplay(true);
                taskDao.insert(task);
            }
        }
    }

    public static boolean checkRecurTask(TaskEntity task, LocalDateTime date) {
        var recurType = task.getRecurType();
        var taskRecurDate = LocalDateTime.ofEpochSecond(task.getRecurDate(),
                0, ZoneOffset.ofHours(-8));

        if (recurType == RecurType.DAILY) {
            return true;
        } else if (recurType == RecurType.WEEKLY) {
            return checkRecurWeekly(taskRecurDate, date);
        } else if (recurType == RecurType.MONTHLY) {
            return checkRecurMonthly(taskRecurDate, date);
        } else {
            return checkRecurYearly(taskRecurDate, date);
        }
    }

    public static boolean checkRecurWeekly(LocalDateTime taskDate, LocalDateTime date) {
        var taskWeekday = taskDate.getDayOfWeek();
        var currentWeekday = date.getDayOfWeek();
        return taskWeekday == currentWeekday;
    }

    public static boolean checkRecurMonthly(LocalDateTime taskDate, LocalDateTime date) {
        var taskNthWeekday = taskDate.get(ChronoField.ALIGNED_WEEK_OF_MONTH);
        var dateNthWeekday = date.get(ChronoField.ALIGNED_WEEK_OF_MONTH);

        // Check if previous month did not contain Nth weekday
        // If above is true, return true if current day is 1st weekday of month
        var prevMonthLastWeekday = YearMonth.of( date.getYear(), date.getMonth().minus(1) )
                .atEndOfMonth()
                .with( TemporalAdjusters.previousOrSame( taskDate.getDayOfWeek() ) );
        var prevMonthNthWeekday = prevMonthLastWeekday.get(ChronoField.ALIGNED_WEEK_OF_MONTH);
        if (prevMonthNthWeekday < taskNthWeekday && dateNthWeekday == 1
            && taskDate.getDayOfWeek() == date.getDayOfWeek()) {
            return true;
        }

        if (taskDate.getDayOfWeek() == date.getDayOfWeek()) {
            return taskNthWeekday == dateNthWeekday;
        }
        return false;
    }

    private static boolean checkRecurYearly(LocalDateTime taskDate, LocalDateTime date) {
        if (taskDate.getDayOfMonth() == 29 && taskDate.getMonth() == Month.FEBRUARY) {
            return taskDate.getDayOfYear() == date.getDayOfYear();
        } else {
            return (taskDate.getDayOfMonth() == date.getDayOfMonth())
                    && (taskDate.getMonth() == date.getMonth());
        }

    }

    private boolean completedBeforeToday(Task task, LocalDateTime date) {
        LocalDateTime timeCompleted = LocalDateTime.ofEpochSecond(task.getCompletedTime(),
                0, ZoneOffset.ofHours(-8));
        return timeCompleted.isBefore(date);
    }

    @Override
    public void completeTask(Task task) {
        // convert to task entity
        TaskEntity taskEntity = fromTask(task);

        boolean isNowCompleted = !taskEntity.isCompleted(); // Assuming changeStatus toggles the completion status.
        taskEntity.changeStatus(); // Toggle the task's completion status.

        // Fetch current sortOrder values to determine the new position of the task
        int minSortOrder = taskDao.getMinSortOrder(); // Implement this to fetch the minimum sortOrder among all tasks.
        int maxSortOrder = taskDao.getMaxSortOrder(); // Implement this to fetch the maximum sortOrder among all tasks.
        int maxIncompleteSortOrder = taskDao.getIncompleteMaxSortOrder(); // Implement this to fetch the maximum sortOrder among incomplete tasks.

        if (isNowCompleted) {
            if (maxIncompleteSortOrder < maxSortOrder) { // There are completed tasks.
                taskDao.shiftSortOrder(maxIncompleteSortOrder + 1, maxSortOrder, 1); // shift completed tasks by 1
                taskEntity = taskEntity.withSortOrder(maxIncompleteSortOrder + 1);
            }
            else { // no completed tasks
                taskEntity = taskEntity.withSortOrder(maxSortOrder + 1);
            }
        }
        else { // uncompleted complete tasks move to the top of the list
            taskDao.shiftSortOrder(minSortOrder, maxSortOrder, 1);
            taskEntity = taskEntity.withSortOrder(minSortOrder);
        }

        // Save the updated task
        taskDao.insert(taskEntity);
    }

    @Override
    public void deleteTask(int id) {
        TaskEntity task = taskDao.find(id);
        taskDao.remove(task.id);
    }

    @Override
    public void deleteCompletedTasks(boolean completed) {
        for (TaskEntity task : taskDao.findAll()) {
            var recurType = task.getRecurType();
            if (task.isCompleted() && (recurType == RecurType.PENDING || recurType == RecurType.ONCE)) {
                taskDao.remove(task.id);
            }
        }
    }

    @Override
    public void deleteCompletedTasksBefore(long cutoffTime) {
        taskDao.deleteCompletedTasksBefore(cutoffTime);
    }
}
