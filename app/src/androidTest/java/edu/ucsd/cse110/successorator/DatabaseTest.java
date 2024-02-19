package edu.ucsd.cse110.successorator;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import static edu.ucsd.cse110.successorator.data.db.TaskEntity.toEntity;

import android.content.Context;

import java.util.List;

import edu.ucsd.cse110.successorator.data.db.RoomTaskRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.data.db.TaskDao;
import edu.ucsd.cse110.successorator.data.db.TaskEntity;
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
    public void completeTask_UpdatesCompletionStatusAndSortOrder() {
        // Assuming you have a helper method to add a task
        Task task = new Task(0, "Test Task", false, 0);
        dao.insert(toEntity(task)); // Insert initial task

        RoomTaskRepository repository = new RoomTaskRepository(dao);
        task.setCompleted(true); // Simulate task completion
        repository.completeTask(task);

        TaskEntity updatedTask = dao.find(task.id());
        assertTrue(updatedTask.isCompleted()); // Verify completion status is updated

        int expectedSortOrder = 1;
        assertEquals(expectedSortOrder, updatedTask.getSortOrder()); // Verify sortOrder is updated
    }


//    @Test
//    public void writeAndReadTaskTest() throws Exception {
//        TaskEntity task = new TaskEntity(0, "task", false, 0);
//        dao.insert(task);
//        TaskEntity byID = dao.find(task.getTaskID());
//        assert byID != null;
//        assertEquals(task.getTaskID(), byID.getTaskID());
//    }

//    @Test
//    public void completeTask_UpdatesCompletionStatusAndSortOrder() {
//        // Assuming you have a helper method to add a task
//        Task task = new Task(null, "Test Task", false, 1);
//        dao.insert(toEntity(task)); // Insert initial task
//
//        RoomTaskRepository repository = new RoomTaskRepository(dao);
//        task.setCompleted(true); // Simulate task completion
//        repository.completeTask(task);
//
//        TaskEntity updatedTask = dao.find(task.id());
//        assertTrue(updatedTask.isCompleted()); // Verify completion status is updated
//
//        // Assuming you have logic to determine expected sortOrder
//        int expectedSortOrder = 2;
//        assertEquals(expectedSortOrder, updatedTask.getSortOrder()); // Verify sortOrder is updated
//    }
}