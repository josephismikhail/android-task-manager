package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class RoomTaskRepository implements TaskRepository {
    private final TaskDao taskDao;

    public RoomTaskRepository(TaskDao taskDao) {
        this.taskDao = taskDao;
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
    public void append(Task task) {
        taskDao.append(TaskEntity.fromTask(task));
    }
}
