package edu.ucsd.cse110.successorator.lib.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

public class InMemoryDataSource {
    private final Map<Integer, Task> tasks
            = new HashMap<>();
    private final Map<Integer, SimpleSubject<Task>> taskSubject
            = new HashMap<>();
    private final SimpleSubject<List<Task>> allTasksSubjects
            = new SimpleSubject<>();

    public InMemoryDataSource() {
    }

    public List<Task> getTasks() {
        return List.copyOf(tasks.values());
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public SimpleSubject<Task> getTaskSubject(int id) {
        if (!taskSubject.containsKey(id)) {
            var subject = new SimpleSubject<Task>();
            subject.setValue(getTask(id));
            taskSubject.put(id, subject);
        }
        return taskSubject.get(id);
    }

    public SimpleSubject<List<Task>> getAllTasksSubjects() {
        return allTasksSubjects;
    }

    public void putTask(Task task) {
        tasks.put(task.id(), task);
        if (taskSubject.containsKey(task.id())) {
            taskSubject.get(task.id()).setValue(task);
        }
        allTasksSubjects.setValue(getTasks());
    }

    public final static List<Task> DEFAULT_CARDS = List.of(
            new Task("Test 1",0),
            new Task("Test 2",1),
            new Task("Test 3",2),
            new Task("Test 4",3),
            new Task("Test 5",4)
    );

    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();
        for (Task task : DEFAULT_CARDS) {
            data.putTask(task);
        }
        return data;
    }
}
