package edu.ucsd.cse110.successorator;

import android.app.Application;

import androidx.room.Room;

import java.util.List;

import edu.ucsd.cse110.successorator.data.db.RoomTaskRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class SuccessoratorApplication extends Application {
//    List<Task> DEFAULT_TASKS = List.of(
//            new Task(1, "Task 1", false, 1, System.currentTimeMillis()),
//            new Task(2, "Task 2", false, 2, System.currentTimeMillis()),
//            new Task(3, "Task 3", false, 3, System.currentTimeMillis()),
//            new Task(4, "Task 4", false, 4, System.currentTimeMillis())
//    );
    private TaskRepository taskRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        var database = Room.databaseBuilder(
                getApplicationContext(),
                SuccessoratorDatabase.class,
                "successorator-database"
        )
                .allowMainThreadQueries()
                .build();
        this.taskRepository = new RoomTaskRepository(database.taskDao());

//        var sharedPreferences = getSharedPreferences("successorator", MODE_PRIVATE);
//        var isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
//        if (isFirstRun && database.taskDao().count() == 0) {
//            taskRepository.save(DEFAULT_TASKS);
//
//            sharedPreferences.edit()
//                    .putBoolean("isFirstRun", false)
//                    .apply();
//        }
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }
}
