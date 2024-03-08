package edu.ucsd.cse110.successorator.lib.domain;

import java.time.LocalDateTime;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.util.Subject;

public interface TaskRepository {

    Subject<Task> find(int id);

    Subject<List<Task>> findAll();

    void save(Task task);

    void save(List<Task> taskList);

    void newTask(Task task);

    void updateDisplayTask(List<Task> taskList, LocalDateTime date);

    void deleteCompletedTasks(boolean completed);

    void deleteCompletedTasksBefore(long cutoffTime);

    void completeTask(Task task, LocalDateTime date);
}
