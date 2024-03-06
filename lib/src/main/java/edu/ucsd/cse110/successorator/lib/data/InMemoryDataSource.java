package edu.ucsd.cse110.successorator.lib.data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

/**
 * The InMemoryDataSource class functions as a local "database" of
 * tasks for the app to run and present on. Will look to implement a
 * remote database so data can persist between sessions.
 */
public class InMemoryDataSource {
    private int nextId = 0;

    private int minSortOrder = Integer.MAX_VALUE;
    private int maxSortOrder = Integer.MIN_VALUE;

    private final Map<Integer, Task> tasks
       = new HashMap<>();
    private LocalDateTime currentTime;
    private final SimpleSubject<LocalDateTime> timeSubject
       = new SimpleSubject<>();
    private final Map<Integer, SimpleSubject<Task>> taskSubjects
       = new HashMap<>();
    private final SimpleSubject<List<Task>> allTasksSubjects
       = new SimpleSubject<>();

    public InMemoryDataSource() {
        currentTime = LocalDateTime.now();
    }

    public final static List<Task> DEFAULT_CARDS = List.of(
       new Task(0, "Test 1", false, 0, null),
       new Task(1, "Test 2", false, 1, null),
       new Task(2, "Test 3", false, 2, null),
       new Task(3, "Test 4", false, 3, null),
       new Task(4, "Test 5", false, 4, null)
    );

    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();
        for (Task task : DEFAULT_CARDS) {
            data.putTask(task);
        }
        return data;
    }

    public Subject<LocalDateTime> getTimeSubject() {
        return timeSubject;
    }

    public void setTime(LocalDateTime dateTime) {
        timeSubject.setValue(dateTime);
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

    /**
     * Ensures that tasks inserted have a valid id
     */
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

    /**
     * Ensures that after insertion of a task that sort orders are still valid
     */
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

    /**
     * Checks to verify that constraints on sort order are maintained
     */
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
