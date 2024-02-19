package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class SimpleTaskRepository implements TaskRepository {
    private final InMemoryDataSource dataSource;

    public SimpleTaskRepository(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Subject<Task> find(int id) {
        return dataSource.getTaskSubject(id);
    }

    @Override
    public Subject<List<Task>> findAll() {
        return dataSource.getAllTasksSubjects();
    }

    @Override
    public void save(Task flashcard) {
        dataSource.putTask(flashcard);
    }

    @Override
    public void save(List<Task> tasks) {
        dataSource.putTasks(tasks);
    }

    @Override
    public void remove(int id) {
        dataSource.removeTask(id);
    }

    @Override
    public int getMinSortOrder() {
        return dataSource.getMinSortOrder();
    }

    @Override
    public int getMaxSortOrder() {
        return 0;
    }

    @Override
    public int getIncompleteMaxSortOrder() {
        return 0;
    }

    @Override
    public void shiftSortOrder(int from, int to, int by) {

    }

    @Override
    public void completeTask(Task task) {

    }

    @Override
    public void prepend(Task task) {
        dataSource.shiftSortOrders(0, dataSource.getMaxSortOrder(), 1);
        dataSource.putTask(
                task.withSortOrder(dataSource.getMinSortOrder() - 1)
        );
    }
}
