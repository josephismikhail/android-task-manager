package edu.ucsd.cse110.successorator;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


import javax.inject.Inject;

import edu.ucsd.cse110.successorator.data.db.RoomTaskRepository;
import edu.ucsd.cse110.successorator.data.db.TaskEntity;
import edu.ucsd.cse110.successorator.lib.domain.RecurType;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;
import edu.ucsd.cse110.successorator.lib.domain.TaskViews;
import edu.ucsd.cse110.successorator.lib.domain.TimeKeeper;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class MainViewModel extends ViewModel {
    // Domain state (true "Model" state)
    private final TaskRepository taskRepository;
    private TaskViews currTaskView;
    private final TimeKeeper timeKeeper;

    // UI state
    private final Subject<List<Task>> unorderedTasks;
    private final SimpleSubject<List<Task>> orderedTasks;

    private List<Task> allTasks;

    private final SimpleSubject<LocalDateTime> currentTime;

    private final SimpleSubject<String> dateDisplayText;


    public static final ViewModelInitializer<MainViewModel> initializer =

        new ViewModelInitializer<>(
            MainViewModel.class,
            creationExtras -> {
                var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                assert app != null;
                return new MainViewModel(app.getTaskRepository(), app.getTimeKeeper());
            });

    @Inject
    public MainViewModel(TaskRepository taskRepository, TimeKeeper timeKeeper) {
        this.taskRepository = taskRepository;

        this.currTaskView = TaskViews.TODAY_VIEW;

        // Create the observable subjects.
        this.orderedTasks = new SimpleSubject<>();
        this.unorderedTasks = taskRepository.findAll();

        // When the list of cards changes (or is first loaded), reset the ordering.
        this.unorderedTasks.observe(this::updateOrderedTasks);

        this.timeKeeper = timeKeeper;

        // Create the observable subjects.
        this.allTasks = new ArrayList<>();
        this.currentTime = new SimpleSubject<>();
        this.dateDisplayText = new SimpleSubject<>();

        // When the list of cards changes (or is first loaded), reset the ordering.
        taskRepository.findAll().observe(tasks -> {
            if (tasks == null) return;

            /*allTasks = tasks.stream()
                    .sorted(Comparator.comparingInt(Task::getSortOrder))
                    .collect(Collectors.toList());
            var newOrderedTasks = allTasks.stream()
                    .filter(Task::display)
                    .collect(Collectors.toList());
            orderedTasks.setValue(newOrderedTasks);*/
            updateOrderedTasks(tasks);
        });
    }

    public Subject<List<Task>> getOrderedTasks() {
        return orderedTasks;
    }

    public void updateDisplayTask(LocalDateTime date) {
        taskRepository.updateDisplayTask(date);
    }

    public void updateDisplayTaskButton() {
        taskRepository.updateDisplayTask(getCurrentTime());
    }

    public Subject<String> getDateDisplayText() { return dateDisplayText; }

    public void save(Task task) {
        taskRepository.save(task);
    }

    public void newTask(Task task) {
        taskRepository.newTask(task);
    }

    public void completeTask(TaskEntity task) {
        taskRepository.completeTask(task.toTask());
    }

    public void deleteTask(int id) {
        taskRepository.deleteTask(id);
    }

    public void deleteCompletedTasks(boolean completed) {
        taskRepository.deleteCompletedTasks(completed);
    }

    public void deleteCompletedTasksBefore(long cutoffTime) {
        taskRepository.deleteCompletedTasksBefore(cutoffTime);
    }

    public TaskViews getCurrTaskView() {
        return this.currTaskView;
    }

    public void switchView(TaskViews nextView) {
        this.currTaskView = nextView;
        updateOrderedTasks(this.unorderedTasks.getValue());
    }

    private void updateOrderedTasks(List<Task> tasks) {
        if (tasks == null) return;
        List<Task> newOrderedTasks = new ArrayList<>();
        switch (this.currTaskView) {
            case TODAY_VIEW:
                // TODO - filter to get only today's tasks
                System.out.println("updated on today");
                newOrderedTasks = tasks.stream()
                        .sorted(Comparator.comparingInt(Task::getSortOrder))
                        .filter(t -> (t.getRecurType() != RecurType.PENDING))
                        .filter(Task::display)
                        .collect(Collectors.toList());
                break;
            case TOMORROW_VIEW:
                // TODO - filter to get only tomorrow's tasks
                System.out.println("updated on tomorrow");
                newOrderedTasks = tasks.stream()
                        .sorted(Comparator.comparingInt(Task::getSortOrder))
                        .filter(t -> (t.getRecurType() != RecurType.PENDING))
                        .filter(t -> RoomTaskRepository.checkRecurTask(TaskEntity.fromTask(t), getCurrentTime().plusDays(1)))
                        .collect(Collectors.toList());
                break;
            case PENDING_VIEW:
                // TODO - filter to get only pending tasks
                newOrderedTasks = tasks.stream()
                        .sorted(Comparator.comparingInt(Task::getSortOrder))
                        .filter(t -> (t.getRecurType() == RecurType.PENDING))
                        .collect(Collectors.toList());
                break;
            case RECURRING_VIEW:
                // TODO - filter to get only recurring tasks
                newOrderedTasks = tasks.stream()
                        .sorted(Comparator.comparingInt(Task::getSortOrder))
                        .filter(t -> ((t.getRecurType() == RecurType.DAILY)
                                    || (t.getRecurType() == RecurType.WEEKLY)
                                    || (t.getRecurType() == RecurType.MONTHLY)
                                    || (t.getRecurType() == RecurType.YEARLY)))
                        .collect(Collectors.toList());
                break;
        }
        orderedTasks.setValue(newOrderedTasks);
    }

    public LocalDateTime getCurrentTime() { return timeKeeper.getDateTime().getValue(); }

    public void setNewTime(LocalDateTime newTime) { timeKeeper.setDateTime(newTime); }
}
