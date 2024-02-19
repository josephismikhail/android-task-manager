package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.util.Subject;

public interface TaskRepository {

    Subject<Task> find(int id);

    Subject<List<Task>> findAll();

    void save(Task task);

    void save(List<Task> taskList);

    void prepend(Task task);

    void remove(int id);

    int getMinSortOrder();

    int getMaxSortOrder();

    int getIncompleteMaxSortOrder();

    void shiftSortOrder(int from, int to, int by);

    void deleteCompletedTasks(boolean completed);

    void deleteCompletedTasksBefore(long cutoffTime);

    void completeTask(Task task);
}
