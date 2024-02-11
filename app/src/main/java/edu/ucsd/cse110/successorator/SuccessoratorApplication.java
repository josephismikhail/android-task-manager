package edu.ucsd.cse110.successorator;

import android.app.Application;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;

public class SuccessoratorApplication extends Application {
    private InMemoryDataSource dataSource;
    private TaskRepository taskRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        this.dataSource = InMemoryDataSource.fromDefault();
        this.taskRepository = new TaskRepository(dataSource);
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }
}
