package edu.ucsd.cse110.successorator;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.util.List;

import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.data.db.TaskDao;
import edu.ucsd.cse110.successorator.data.db.TaskEntity;
import edu.ucsd.cse110.successorator.lib.domain.Task;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    private SuccessoratorDatabase db; // Corrected to use your SuccessoratorDatabase
    private TaskDao dao;

    @Before
    public void createDb() {
        db = Room.inMemoryDatabaseBuilder(
                        ApplicationProvider.getApplicationContext(),
                        SuccessoratorDatabase.class) // Corrected to SuccessoratorDatabase
                .allowMainThreadQueries()
                .build();
        dao = db.taskDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void writeAndReadTaskTest() throws Exception {
        TaskEntity task = new TaskEntity(0, "task", false, 0);
        dao.insert(task);
        TaskEntity byID = dao.find(task.getTaskID());
        assert byID != null;
        assertEquals(task.getTaskID(), byID.getTaskID());
    }

//    @Test
//    public void completeTaskTest() throws Exception {
//        // Assuming dao is your data access object (DAO) and db is your database instance
//        TaskEntity task1 = new TaskEntity(0, "task 1", false, 0);
//        dao.insert(task1); // Insert task1
//
//        // Perform completion operation that also updates sortOrder,
//        // you need to implement this method in your DAO
//        dao.completeTask(task1.getTaskID(), true);
//
//        // Retrieve the updated task
//        TaskEntity updatedTask = LiveDataTestUtil.getOrAwaitValue(dao.findAsLiveData(task1.getTaskID()));
//
//        // Verify that the sortOrder is updated as expected
//        assertEquals(4, updatedTask.sortOrder);
//    }
}

