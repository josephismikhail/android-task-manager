package edu.ucsd.cse110.successorator.data.db;

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

    @Override
    public void deleteCompletedTasks(boolean completed) {
        taskDao.deleteCompletedTasks(completed);
    }
}
