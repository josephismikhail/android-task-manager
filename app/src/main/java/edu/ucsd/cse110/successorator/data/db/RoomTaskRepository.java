package edu.ucsd.cse110.successorator.data.db;

import static edu.ucsd.cse110.successorator.data.db.TaskEntity.toEntity;

import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.stream.Collectors;

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
        taskDao.insert(TaskEntity.fromTask(task));
    }

    @Override
    public void save(List<Task> taskList) {
        var entities = taskList.stream()
                .map(TaskEntity::fromTask)
                .collect(Collectors.toList());
        taskDao.insert(entities);
    }

    @Override
    public void prepend(Task task) {
        taskDao.prepend(TaskEntity.fromTask(task));
    }

    @Override
    public void remove(int id) {
        taskDao.remove(id);
    }

    @Override
    public int getMinSortOrder() {
        return taskDao.getMinSortOrder();
    }

    @Override
    public int getMaxSortOrder() {
        return taskDao.getMaxSortOrder();
    }

    @Override
    public int getIncompleteMaxSortOrder() {
        return taskDao.getIncompleteMaxSortOrder();
    }

    @Override
    public void shiftSortOrder(int from, int to, int by) {
        taskDao.shiftSortOrder(from, to, by);
    }

    public void completeTask(Task task) {
        // convert to task entity
        TaskEntity taskEntity = toEntity(task);

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
    public void deleteCompletedTasks(boolean completed) {
        for (TaskEntity task : taskDao.findAll()) {
            if (task.completed) {
                taskDao.remove(task.id);
            }
        }
    }

    @Override
    public void deleteCompletedTasksBefore(long cutoffTime) {
        taskDao.deleteCompletedTasksBefore(cutoffTime);
    }
}
