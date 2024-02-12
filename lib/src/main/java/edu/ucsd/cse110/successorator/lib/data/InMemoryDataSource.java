package edu.ucsd.cse110.successorator.lib.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

public class InMemoryDataSource {
    private int nextId = 0;

    private int minSortOrder = Integer.MAX_VALUE;
    private int maxSortOrder = Integer.MIN_VALUE;

    private final Map<Integer, Task> tasks
            = new HashMap<>();
    private final Map<Integer, SimpleSubject<Task>> taskSubjects
            = new HashMap<>();
    private final SimpleSubject<List<Task>> allTasksSubjects
            = new SimpleSubject<>();

    public InMemoryDataSource() {
    }

    public final static List<Task> DEFAULT_CARDS = List.of(
            new Task("Test 1",0, 0),
            new Task("Test 2",1, 1),
            new Task("Test 3",2, 2),
            new Task("Test 4",3, 3),
            new Task("Test 5",4, 4)
    );

    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();
        for (Task task : DEFAULT_CARDS) {
            data.putTask(task);
        }
        return data;
    }

    public List<Task> getTasks() {
        return List.copyOf(tasks.values());
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public SimpleSubject<Task> getTaskSubject(int id) {
        if (!taskSubjects.containsKey(id)) {
            var subject = new SimpleSubject<Task>();
            subject.setValue(getTask(id));
            taskSubjects.put(id, subject);
        }
        return taskSubjects.get(id);
    }

    public SimpleSubject<List<Task>> getAllTasksSubjects() {
        return allTasksSubjects;
    }

    public int getMinSortOrder() {
        return minSortOrder;
    }

    public int getMaxSortOrder() {
        return maxSortOrder;
    }

    public void putTask(Task task) {
        var fixedTask = preInsert(task);
        tasks.put(fixedTask.id(), fixedTask);
        postInsert();
        assertSortOrderConstraints();

        if (taskSubjects.containsKey(fixedTask.id())) {
            taskSubjects.get(fixedTask.id()).setValue(fixedTask);
        }
        allTasksSubjects.setValue(getTasks());
    }

    public void putTasks(List<Task> items) {
        var fixedTasks = items.stream()
                .map(this::preInsert)
                .collect(Collectors.toList());

        fixedTasks.forEach(task -> tasks.put(task.id(), task));
        postInsert();
        assertSortOrderConstraints();
    }

    public void removeTask(int id) {
        var task = tasks.get(id);
        var sortOrder = task.sortOrder();

        tasks.remove(id);
        shiftSortOrders(sortOrder, maxSortOrder, -1);

        if (taskSubjects.containsKey(id)) {
            taskSubjects.get(id).setValue(null);
        }
        allTasksSubjects.setValue(getTasks());
    }

    public void shiftSortOrders(int from, int to, int by) {
        var items = tasks.values().stream()
                .filter(task -> task.sortOrder() >= from && task.sortOrder() <= to)
                .map(task -> task.withSortOrder(task.sortOrder() + by))
                .collect(Collectors.toList());
        putTasks(items);
    }

    private Task preInsert(Task task) {
        var id = task.id();
        if (id == null) {
            task = task.withId(nextId++);
        }
        else if (id > nextId) {
            nextId = id + 1;
        }
        return task;
    }

    private void postInsert() {
        minSortOrder = tasks.values().stream()
                .map(Task::sortOrder)
                .min(Integer::compareTo)
                .orElse(Integer.MAX_VALUE);
        maxSortOrder = tasks.values().stream()
                .map(Task::sortOrder)
                .max(Integer::compareTo)
                .orElse(Integer.MIN_VALUE);
    }

    private void assertSortOrderConstraints() {
        var sortOrders = tasks.values().stream()
                .map(Task::sortOrder)
                .collect(Collectors.toList());

        assert sortOrders.stream().allMatch(i -> i >= 0);
        assert sortOrders.size() == sortOrders.stream().distinct().count();
        assert sortOrders.stream().allMatch(i -> i >= minSortOrder);
        assert sortOrders.stream().allMatch(i -> i <= maxSortOrder);
    }
}
