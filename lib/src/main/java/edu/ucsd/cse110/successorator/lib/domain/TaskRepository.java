package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

public class TaskRepository {
    private final InMemoryDataSource dataSource;

    public TaskRepository(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Integer count() {
        return dataSource.getTasks().size();
    }

    public SimpleSubject<Task> find(int id) {
        return dataSource.getTaskSubject(id);
    }

    public SimpleSubject<List<Task>> findAll() {
        return dataSource.getAllTasksSubjects();
    }

    public void save(Task task) {
        dataSource.putTask(task);
    }
}
