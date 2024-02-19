package edu.ucsd.cse110.successorator;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


import javax.inject.Inject;

import edu.ucsd.cse110.successorator.data.db.RoomTaskRepository;
import edu.ucsd.cse110.successorator.data.db.TaskEntity;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class MainViewModel extends ViewModel {
    // Domain state (true "Model" state)
    private final TaskRepository taskRepository;

    // UI state
    private final SimpleSubject<List<Task>> orderedTasks;

    public static final ViewModelInitializer<MainViewModel> initializer =
        new ViewModelInitializer<>(
            MainViewModel.class,
            creationExtras -> {
                var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                assert app != null;
                return new MainViewModel(app.getTaskRepository());
            });

    @Inject
    public MainViewModel(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;

        // Create the observable subjects.
        this.orderedTasks = new SimpleSubject<>();

        // When the list of cards changes (or is first loaded), reset the ordering.
        taskRepository.findAll().observe(tasks -> {
            if (tasks == null) return;

            var newOrderedTasks = tasks.stream()
                .sorted(Comparator.comparingInt(Task::sortOrder))
                .collect(Collectors.toList());
            orderedTasks.setValue(newOrderedTasks);
        });

    }

    public Subject<List<Task>> getOrderedTasks() {
        return orderedTasks;
    }

    public int getMinSortOrder() {
        return taskRepository.getMinSortOrder();
    }

    public int getMaxSortOrder() {
        return taskRepository.getMaxSortOrder();
    }

    public int getIncompleteMaxSortOrder() {
        return taskRepository.getIncompleteMaxSortOrder();
    }

    public void shiftSortOrder(int from, int to, int by) {
        taskRepository.shiftSortOrder(from, to, by);
    }

    public void save(Task task) { taskRepository.save(task); }

    public void prepend(Task task) {
        taskRepository.prepend(task);
    }

    public void remove(int id) { taskRepository.remove(id); }

    public void deleteCompletedTasks(boolean completed) {taskRepository.deleteCompletedTasks(completed);}

    public void deleteCompletedTasksBefore(long cutoffTime) {taskRepository.deleteCompletedTasksBefore(cutoffTime);}


    public void completeTask(TaskEntity task) {
        // Delegate the operation to the repository
        taskRepository.completeTask(task.toTask());
    }
}
