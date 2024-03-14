package edu.ucsd.cse110.successorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.data.db.TaskDao;
import edu.ucsd.cse110.successorator.data.db.TaskEntity;
import edu.ucsd.cse110.successorator.data.db.TestRoomTaskRepository;
import edu.ucsd.cse110.successorator.lib.data.TestableInMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.RecurType;
import edu.ucsd.cse110.successorator.lib.domain.SimpleTimeKeeper;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.util.Observer;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;

@RunWith(AndroidJUnit4.class)
public class MainViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    private MainViewModel viewModel;
    private TaskDao taskDao;
    private TestRoomTaskRepository testTaskRepository;
    private SimpleTimeKeeper timeKeeper;
    private TestableInMemoryDataSource testableInMemoryDataSource; // Reference to the data source

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        SuccessoratorDatabase db = Room.inMemoryDatabaseBuilder(context, SuccessoratorDatabase.class)
                .allowMainThreadQueries()
                .build();
        taskDao = db.taskDao();
        testTaskRepository = new TestRoomTaskRepository(taskDao);

        // Initialize the TestableInMemoryDataSource with an initial time
        LocalDateTime initialTime = LocalDateTime.now();
        testableInMemoryDataSource = new TestableInMemoryDataSource(initialTime);

        // Pass the data source to the timekeeper
        timeKeeper = new SimpleTimeKeeper(testableInMemoryDataSource);

        // Instantiate your MainViewModel with the real dependencies
        viewModel = new MainViewModel(testTaskRepository, timeKeeper);
    }

//    @Test
//    public void testGetOrderedTasks() throws InterruptedException {
//        final CountDownLatch latch = new CountDownLatch(1);
//        final List[] tasksFromSubject = new List[1];
//
//        viewModel.getOrderedTasks().observe(new Observer<List<Task>>() {
//            @Override
//            public void onChanged(List<Task> tasks) {
//                tasksFromSubject[0] = tasks;
//                latch.countDown(); // Release the latch when data is received
//            }
//        });
//
//        // Create and insert tasks into the repository
//        Task task1 = new Task(1, "Task 1", false, 1, System.currentTimeMillis(), TaskContext.HOME, RecurType.ONCE, null, true);
//        Task task2 = new Task(2, "Task 2", false, 2, System.currentTimeMillis() + 1000, TaskContext.HOME, RecurType.ONCE, null, true);
//        testTaskRepository.newTask(task1);
//        testTaskRepository.newTask(task2);
//
//        latch.await(2, TimeUnit.SECONDS); // Wait for the observer to receive the data
//
//        // Assertions
//        assertNotNull(tasksFromSubject[0]);
//        assertEquals(2, tasksFromSubject[0].size()); // Assuming you expect 2 tasks
//        assertTrue(tasksFromSubject[0].contains(task1));
//        assertTrue(tasksFromSubject[0].contains(task2));
//    }

    @Test
    public void testUpdateDisplayTask() {
        LocalDateTime testDate = LocalDateTime.now();
        viewModel.updateDisplayTask(testDate);

        assertEquals(testDate, testTaskRepository.getLastUpdatedDate());
    }

    @Test
    public void testUpdateDisplayTaskButton() {
        // Arrange: Set a known time in the TestableInMemoryDataSource
        LocalDateTime knownTime = LocalDateTime.of(2020, 10, 10, 10, 10); // Example time
        testableInMemoryDataSource.setTime(knownTime);

        // Act: Call the method to test
        viewModel.updateDisplayTaskButton();

        // Assert: Verify that the known time was used to update the display task
        assertEquals(knownTime, testTaskRepository.getLastUpdatedDate());
    }

//    @Test
//    public void testNewTask() {
//        Task task = new Task(1, "New Task", false, 1, System.currentTimeMillis(), TaskContext.HOME, RecurType.ONCE, null, true);
//
//        viewModel.newTask(task);
//
//        // Verify that the taskRepository received the correct task
//        assertNotNull(testTaskRepository.getLastTask());
//        assertEquals(task.getId(), testTaskRepository.getLastTask().getId());
//        assertEquals(task.getTask(), testTaskRepository.getLastTask().getTask());
//    }

//    @Test
//    public void testCompleteTask() {
//        TaskEntity taskEntity = new TaskEntity(1, "Complete Task", false, 1, System.currentTimeMillis(), TaskContext.HOME, RecurType.ONCE, null, true);
//
//        viewModel.completeTask(taskEntity);
//
//        // Verify that the taskRepository received the correct task conversion
//        assertNotNull(testTaskRepository.getLastCompletedTask());
//        assertEquals(Integer.valueOf(taskEntity.getTaskID()), testTaskRepository.getLastCompletedTask().getId());
//        assertEquals(taskEntity.getTask(), testTaskRepository.getLastCompletedTask().getTask());
//    }

    @Test
    public void testDeleteCompletedTasks() {
        // Test with true
        viewModel.deleteCompletedTasks(true);
        assertNotNull(testTaskRepository.getLastDeletedCompletedStatus());
        assertTrue(testTaskRepository.getLastDeletedCompletedStatus());

        // Test with false
        viewModel.deleteCompletedTasks(false);
        assertNotNull(testTaskRepository.getLastDeletedCompletedStatus());
        assertFalse(testTaskRepository.getLastDeletedCompletedStatus());
    }
}