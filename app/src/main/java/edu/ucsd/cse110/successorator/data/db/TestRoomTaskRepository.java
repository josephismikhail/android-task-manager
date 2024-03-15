package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import edu.ucsd.cse110.successorator.lib.domain.SimpleTimeKeeper;
import edu.ucsd.cse110.successorator.lib.domain.Task;

public class TestRoomTaskRepository extends RoomTaskRepository {
    private Task lastTask = null;
    private LocalDateTime lastUpdatedDate = null;
    private Task lastCompletedTask = null;
    private Boolean lastDeletedCompletedStatus = null;
    private final SimpleTimeKeeper timeKeeper;

    public TestRoomTaskRepository(TaskDao taskDao, SimpleTimeKeeper timeKeeper) {
        super(taskDao);
        this.timeKeeper = timeKeeper;
    }


    @Override
    public void updateDisplayTask(LocalDateTime date) {
        super.updateDisplayTask(date);
        lastUpdatedDate = date;
    }

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    @Override
    public void newTask(Task task) {
        super.newTask(task);
        this.lastTask = task;
    }

    @Override
    public void completeTask(Task task) {
        super.completeTask(task);
        this.lastCompletedTask = task;
    }

    @Override
    public void deleteCompletedTasks(boolean completed) {
        super.deleteCompletedTasks(completed);
        this.lastDeletedCompletedStatus = completed;
    }

    public Boolean getLastDeletedCompletedStatus() {
        return lastDeletedCompletedStatus;
    }

    public Task findTaskByName(String taskName) {
        LiveData<List<TaskEntity>> liveData = getTaskDao().findAllAsLiveData();
        List<TaskEntity> taskEntities = getValue(liveData); // Block and wait for LiveData to emit the value

        if (taskEntities == null) return null;

        for (TaskEntity taskEntity : taskEntities) {
            if (taskEntity.getTask().equals(taskName)) {
                return taskEntity.toTask();
            }
        }
        return null;
    }

    // Utility method to fetch LiveData value (use only in tests)
    private <T> T getValue(LiveData<T> liveData) {
        final Object[] data = new Object[1];
        CountDownLatch latch = new CountDownLatch(1);
        liveData.observeForever(o -> {
            data[0] = o;
            latch.countDown();
        });

        try {
            latch.await(2, TimeUnit.SECONDS); // Wait for LiveData to emit
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return (T) data[0];
    }

    public boolean isTaskPresentOnSameDayOfWeek(String taskName, LocalDateTime dateTime) {
        Task task = findTaskByName(taskName);
        if (task == null || task.getCompletedTime() == null) {
            return false;
        }

        LocalDateTime taskCompletedDateTime = LocalDateTime.ofEpochSecond(task.getCompletedTime(), 0, ZoneOffset.UTC);
        return taskCompletedDateTime.getDayOfWeek() == dateTime.getDayOfWeek();
    }
}

