package edu.ucsd.cse110.successorator;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import static edu.ucsd.cse110.successorator.data.db.TaskEntity.fromTask;
import static edu.ucsd.cse110.successorator.lib.domain.TaskContext.HOME;

import android.content.Context;

import java.util.List;

import edu.ucsd.cse110.successorator.data.db.RoomTaskRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.data.db.TaskDao;
import edu.ucsd.cse110.successorator.data.db.TaskEntity;
import edu.ucsd.cse110.successorator.lib.domain.RecurType;
import edu.ucsd.cse110.successorator.lib.domain.Task;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    TaskDao dao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        // Allowing main thread queries for simplicity in tests
        dao = Room.inMemoryDatabaseBuilder(context, SuccessoratorDatabase.class)
                .allowMainThreadQueries() // Allowing main thread queries for simplicity in tests
                .build()
                .taskDao();
    }

    @Test
    public void writeAndReadTaskTest() throws Exception {
        var testTask = new Task(0, "task", false, 0,
                System.currentTimeMillis(), HOME, RecurType.ONCE, null, true);
        TaskEntity task = fromTask(testTask);
        dao.insert(task);
        TaskEntity byID = dao.find(task.getTaskID());
        assert byID != null;
        assertEquals(task.getTaskID(), byID.getTaskID());
    }

    @Test
    public void completeOneTaskTest() {
        Task task = new Task(0, "Test Task", false, 0,
                System.currentTimeMillis(), HOME, RecurType.ONCE, null, true);
        dao.insert(fromTask(task)); // Insert initial task

        RoomTaskRepository repository = new RoomTaskRepository(dao);
        repository.completeTask(task); // Simulate completing task

        TaskEntity updatedTask = dao.find(task.getId());
        assertTrue(updatedTask.isCompleted()); // Verify completion status is updated

        int expectedSortOrder = 1;
        assertEquals(expectedSortOrder, updatedTask.getSortOrder()); // Verify sortOrder is updated
    }

    @Test
    public void completeTwoTasksTest() {
        Task task1 = new Task(0, "Test Task 1", false, 0,
                System.currentTimeMillis(), HOME, RecurType.ONCE, null, true);
        Task task2 = new Task(1, "Test Task 2", false, 1,
                System.currentTimeMillis(), HOME, RecurType.ONCE, null, true);
        dao.insert(fromTask(task1));
        dao.insert(fromTask(task2));

        RoomTaskRepository repository = new RoomTaskRepository(dao);
        repository.completeTask(task1);

        TaskEntity updatedTask1 = dao.find(task1.getId());
        assertTrue(updatedTask1.isCompleted());

        assertEquals(2, updatedTask1.getSortOrder());

        repository.completeTask(task2);

        updatedTask1 = dao.find(task1.getId());
        TaskEntity updatedTask2 = dao.find(task2.getId());
        assertTrue(updatedTask2.isCompleted());

        assertEquals(2, updatedTask2.getSortOrder());
        assertTrue(updatedTask1.getSortOrder() > updatedTask2.getSortOrder());
    }

    @Test
    public void uncompleteTwoTasksTest() {
        Task task1 = new Task(0, "Test Task 1", true, 0,
                System.currentTimeMillis(), HOME, RecurType.ONCE, null, true);
        Task task2 = new Task(1, "Test Task 2", true, 1,
                System.currentTimeMillis(), HOME, RecurType.ONCE, null, true);
        dao.insert(fromTask(task1));
        dao.insert(fromTask(task2));

        RoomTaskRepository repository = new RoomTaskRepository(dao);

        // uncomplete task 2
        repository.completeTask(task2);

        // update
        TaskEntity updatedTask2 = dao.find(task2.getId());

        // check
        assertFalse(updatedTask2.isCompleted());

        TaskEntity updatedTask1 = dao.find(task1.getId());

        assertTrue(updatedTask2.getSortOrder() < updatedTask1.getSortOrder());

        // uncomplete task 1
        repository.completeTask(task1);

        // update
        updatedTask1 = dao.find(task1.getId());
        updatedTask2 = dao.find(task2.getId());

        // check
        assertFalse(updatedTask1.isCompleted());

        assertTrue(updatedTask2.getSortOrder() > updatedTask1.getSortOrder());
    }
}