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

import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.data.db.TaskDao;
import edu.ucsd.cse110.successorator.data.db.TestRoomTaskRepository;
import edu.ucsd.cse110.successorator.lib.data.TestableInMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.SimpleTimeKeeper;

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
        testTaskRepository = new TestRoomTaskRepository(taskDao, timeKeeper);

        // Initialize the TestableInMemoryDataSource with an initial time
        LocalDateTime initialTime = LocalDateTime.now();
        testableInMemoryDataSource = new TestableInMemoryDataSource(initialTime);

        // Pass the data source to the timekeeper
        timeKeeper = new SimpleTimeKeeper(testableInMemoryDataSource);

        // Instantiate your MainViewModel with the real dependencies
        viewModel = new MainViewModel(testTaskRepository, timeKeeper);
    }

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