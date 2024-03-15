package edu.ucsd.cse110.successorator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.data.db.TaskDao;
import edu.ucsd.cse110.successorator.data.db.TaskEntity;
import edu.ucsd.cse110.successorator.data.db.TestRoomTaskRepository;
import edu.ucsd.cse110.successorator.lib.data.TestableInMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.SimpleTimeKeeper;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.RecurType;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;
import edu.ucsd.cse110.successorator.lib.domain.TaskViews;
import edu.ucsd.cse110.successorator.ui.cardlist.dialog.CreatePendingTaskDialogFragment;

public class BDDScenariosTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MainViewModel viewModel;
    private SimpleTimeKeeper timeKeeper;
    private TaskRepository taskRepository; // Your task repository implementation
    TestableInMemoryDataSource testableInMemoryDataSource;
    TaskDao taskDao;
    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        SuccessoratorDatabase db = Room.inMemoryDatabaseBuilder(context, SuccessoratorDatabase.class)
                .allowMainThreadQueries()
                .build();
        taskDao = db.taskDao();
        taskRepository = new TestRoomTaskRepository(taskDao, timeKeeper);

        // Initialize the TestableInMemoryDataSource with an initial time
        LocalDateTime initialTime = LocalDateTime.now();
        testableInMemoryDataSource = new TestableInMemoryDataSource(initialTime);

        // Pass the data source to the timekeeper
        timeKeeper = new SimpleTimeKeeper(testableInMemoryDataSource);

        // Instantiate your MainViewModel with the real dependencies
        viewModel = new MainViewModel(taskRepository, timeKeeper);
    }

    /**
     *      User Story 1
     *      Recurring Tasks
     *      BDD Scenario 3 - Create Weekly Task
     **/
    @Test
    public void testCreateWeeklyTask() {
        //  Given I want to set up a homework-due alarm every Sunday at 11:59PM
        //  And today is Sunday
        LocalDateTime sundayAtElevenFiftyNine = LocalDate.parse("2024-03-17").atTime(23, 59);
        timeKeeper.setDateTime(sundayAtElevenFiftyNine);

        //  When I click on the “+” button
        //  And type the task name “submit CSE110 homework”
        //  And select the “weekly on Sun” radio button
        //  And taps save
        Task task = new Task(0, "submit CSE110 homework", false, -1, null, TaskContext.SCHOOL, RecurType.WEEKLY, sundayAtElevenFiftyNine.toEpochSecond(ZoneOffset.UTC), true);
        viewModel.newTask(task);

        //  Then I can see the task named “submit CSE110 homework, weekly on Sun”
        Task fetchedTask = taskRepository.findTaskByName("submit CSE110 homework");
        assertNotNull(fetchedTask);
        assertEquals(RecurType.WEEKLY, fetchedTask.getRecurType());

        //  When I recheck the app the next day at 2:00 AM (I completed the task yesterday)
        viewModel.completeTask(TaskEntity.fromTask(task));
        LocalDateTime nextDayAtTwoAM = sundayAtElevenFiftyNine.plusDays(1).withHour(2).withMinute(0);
        timeKeeper.setDateTime(nextDayAtTwoAM);

        //  Then “submit CSE110 homework, weekly on Sun” disappears
        assertFalse(taskRepository.isTaskPresentOnSameDayOfWeek("submit CSE110 homework", nextDayAtTwoAM));
    }

    /**
     *      User Story 2
     *      New Tasks Views
     *      BDD Scenario 4 - Rollover unfinished tasks from “Tomorrow” view to next day’s “Today”
     **/
    @Test
    public void testTaskRolloverFromTomorrowToToday() {
        //  Given I have a unfinished task “Buy flowers” on “Tomorrow”
        LocalDateTime initialTime = LocalDateTime.of(2024, 2, 20, 10, 0);
        timeKeeper.setDateTime(initialTime);
        LocalDateTime dueDate = initialTime.plusDays(1);
        Task task = new Task(null, "Buy flowers", false, 0, null, TaskContext.HOME, RecurType.ONCE, dueDate.toEpochSecond(ZoneOffset.UTC), true);
        viewModel.newTask(task);

        //  When I enter the app next morning after 2:00 AM
        LocalDateTime nextMorning = initialTime.plusDays(1).withHour(3);
        timeKeeper.setDateTime(nextMorning);
        viewModel.updateDisplayTask(nextMorning);

        //  Then I see the “Buy flowers” task on “Today” and not on “Tomorrow”
        viewModel.switchView(TaskViews.TODAY_VIEW);
        List<Task> todayTasks = viewModel.getOrderedTasks().getValue();
        assert todayTasks != null;
        assertTrue(containsTask(todayTasks, "Buy flowers"));

        // checking "Tomorrow"
        viewModel.switchView(TaskViews.TOMORROW_VIEW);
        List<Task> tomorrowTasks = viewModel.getOrderedTasks().getValue();
        assertFalse(containsTask(tomorrowTasks, "Buy flowers"));
    }

    /**
     *      User Story 3
     *      Pending Tasks
     *      BDD Scenario 1 - Create a task in “Pending" view
     **/
    @Test
    public void testCreateTaskInPendingView() {
        // Given: Context is set - want to book a plane ticket

        // When: Create a task marked as Pending
        Task pendingTask = new Task(null, "book a plane ticket", false, 0, null, TaskContext.ERRAND, RecurType.PENDING, null, true);
        viewModel.newTask(pendingTask);

        // Then: Verify the task appears in the Pending view
        viewModel.switchView(TaskViews.PENDING_VIEW);
        List<Task> pendingTasks = viewModel.getOrderedTasks().getValue();
        assert pendingTasks != null;
        assertTrue(containsTask(pendingTasks, "book a plane ticket"));

        // And: Verify the task does not appear in the Today view
        viewModel.switchView(TaskViews.TODAY_VIEW);
        List<Task> todayTasks = viewModel.getOrderedTasks().getValue();
        assertFalse(containsTask(todayTasks, "book a plane ticket"));

        // And: Verify the task does not appear in the Tomorrow view
        viewModel.switchView(TaskViews.TOMORROW_VIEW);
        List<Task> tomorrowTasks = viewModel.getOrderedTasks().getValue();
        assertFalse(containsTask(tomorrowTasks, "book a plane ticket"));

        // And: Verify the task does not appear in the Recurring view
        viewModel.switchView(TaskViews.RECURRING_VIEW);
        List<Task> recurringTasks = viewModel.getOrderedTasks().getValue();
        assertFalse(containsTask(recurringTasks, "book a plane ticket"));
    }

    /**
     *      User Story 5
     *      Contextual Task Management
     *      BDD Scenario 1 - Create a task with a Context
     **/
    @Test
    public void testCreateTaskWithContext() {
        // When creating a task with "Work" context
        Task newTask = new Task(null, "Grade exams", false, 0, null, TaskContext.WORK, RecurType.PENDING, System.currentTimeMillis() / 1000, true);
        viewModel.newTask(newTask);

        // Then: Verify the task appears in the Pending view
        viewModel.switchView(TaskViews.PENDING_VIEW);
        List<Task> pendingTasks = viewModel.getOrderedTasks().getValue();
        assert pendingTasks != null;
        assertTrue(containsTaskWithSpecificContext(pendingTasks, "Grade exams", TaskContext.WORK));

        // And: Verify the task does not appear in the Today view
        viewModel.switchView(TaskViews.TODAY_VIEW);
        List<Task> todayTasks = viewModel.getOrderedTasks().getValue();
        assertFalse(containsTask(todayTasks, "Grade exams"));

        // And: Verify the task does not appear in the Tomorrow view
        viewModel.switchView(TaskViews.TOMORROW_VIEW);
        List<Task> tomorrowTasks = viewModel.getOrderedTasks().getValue();
        assertFalse(containsTask(tomorrowTasks, "Grade exams"));

        // And: Verify the task does not appear in the Recurring view
        viewModel.switchView(TaskViews.RECURRING_VIEW);
        List<Task> recurringTasks = viewModel.getOrderedTasks().getValue();
        assertFalse(containsTask(recurringTasks, "Grade exams"));
    }

    // Helper method to check if a list of tasks contains a task with a specific name
    private boolean containsTask(List<Task> tasks, String taskName) {
        return tasks.stream().anyMatch(task -> task.getTask().equals(taskName));
    }

    // Helper method to check if a list of tasks contains a task with a specific name & context
    private boolean containsTaskWithSpecificContext(List<Task> tasks, String taskName, TaskContext context) {
        return tasks.stream().anyMatch(task -> task.getTask().equals(taskName) && task.getContext() == context);
    }
}
