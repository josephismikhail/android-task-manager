package edu.ucsd.cse110.successorator;
//
//import androidx.lifecycle.LiveData;
//import androidx.room.Room;
//import androidx.test.core.app.ApplicationProvider;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import static org.junit.Assert.*;
//
//import static edu.ucsd.cse110.successorator.data.db.TaskEntity.fromTask;
//
//import android.content.Context;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import edu.ucsd.cse110.successorator.data.db.RoomTaskRepository;
//import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
//import edu.ucsd.cse110.successorator.data.db.TaskDao;
//import edu.ucsd.cse110.successorator.data.db.TaskEntity;
//import edu.ucsd.cse110.successorator.lib.domain.RecurType;
//import edu.ucsd.cse110.successorator.lib.domain.Task;
//import edu.ucsd.cse110.successorator.lib.util.Subject;
//import edu.ucsd.cse110.successorator.util.LiveDataTestUtil;
//
//
//@RunWith(AndroidJUnit4.class)
public class RoomTaskRepositoryTest {
//    private SuccessoratorDatabase db;
//    private TaskDao dao;
//    private RoomTaskRepository repository;
//
//    @Before
//    public void createDb() {
//        Context context = ApplicationProvider.getApplicationContext();
//        db  = Room.inMemoryDatabaseBuilder(context, SuccessoratorDatabase.class)
//                .allowMainThreadQueries()
//                .build();
//        dao = db.taskDao();
//        repository = new RoomTaskRepository(dao);
//    }
//
//    @Test
//    public void testFindAll() throws InterruptedException {
//        Task task1 = new Task(null, "Task 1", false, 1, System.currentTimeMillis(), RecurType.ONCE, null, true);
//        Task task2 = new Task(null, "Task 2", true, 2, System.currentTimeMillis(), RecurType.ONCE, null, false);
//        dao.insert(fromTask(task1));
//        dao.insert(fromTask(task2));
//
//        Subject<List<Task>> tasksSubject = repository.findAll();
//
//        List<Task> tasks = LiveDataTestUtil.getOrAwaitValue((LiveData<List<Task>>) tasksSubject);
//
//        assertNotNull(tasks);
//        assertEquals(2, tasks.size());
//    }
//
//
//    @Test
//    public void testNewTask() {
//        Task task1 = new Task(null, "Task 1", false, 1, System.currentTimeMillis(), RecurType.ONCE, null, true);
//        repository.newTask(task1);
//
//        TaskEntity taskEntity = dao.find(task1.getId());
//        assertNotNull(taskEntity);
//        assertEquals(1, taskEntity.getSortOrder());
//    }
//
//    @Test
//    public void testUpdateDisplayTask() {
//        // Assuming there are methods to convert LocalDateTime to epoch seconds
//        LocalDateTime now = LocalDateTime.now();
//        Task task1 = new Task(1, "Task 1", true, 1, System.currentTimeMillis(), RecurType.DAILY, null, true);
//        dao.insert(fromTask(task1));
//
//        repository.updateDisplayTask(now.plusDays(1)); // Assuming task should be displayed again due to DAILY recurrence
//
//        TaskEntity taskEntity = dao.find(task1.getId());
//        assertTrue(taskEntity.isDisplay());
//    }
//
//    @Test
//    public void testDeleteCompletedTasks() {
//        Task task1 = new Task(1, "Task 1", true, 1, System.currentTimeMillis(), null, null, true);
//        dao.insert(fromTask(task1));
//
//        repository.deleteCompletedTasks(true);
//
//        assertNull(dao.find(task1.getId()));
//    }
//
//    @Test
//    public void testDeleteCompletedTasksBefore() {
//        long cutoffTime = System.currentTimeMillis();
//        Task task1 = new Task(1, "Task 1", true, 1, cutoffTime - 10000, RecurType.ONCE, null, true); // Completed before cutoff
//        Task task2 = new Task(2, "Task 2", true, 2, cutoffTime + 10000, RecurType.ONCE, null, true); // Completed after cutoff
//        dao.insert(fromTask(task1));
//        dao.insert(fromTask(task2));
//
//        repository.deleteCompletedTasksBefore(cutoffTime);
//
//        assertNull(dao.find(task1.getId()));
//        assertNotNull(dao.find(task2.getId()));
//    }
//
//    // Ensure you close the database after each test to clean up
//    @After
//    public void closeDb() {
//        db.close();
//    }
}
