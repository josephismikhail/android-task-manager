package edu.ucsd.cse110.successorator.data.db;

import java.time.LocalDateTime;

import edu.ucsd.cse110.successorator.lib.domain.Task;

public class TestRoomTaskRepository extends RoomTaskRepository {
    private Task lastTask = null;
    private LocalDateTime lastUpdatedDate = null;
    private Task lastCompletedTask = null;
    private Boolean lastDeletedCompletedStatus = null;

    public TestRoomTaskRepository(TaskDao taskDao) {
        super(taskDao);
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

    public Task getLastTask() {
        return lastTask;
    }

    @Override
    public void completeTask(Task task) {
        super.completeTask(task);
        this.lastCompletedTask = task;
    }

    public Task getLastCompletedTask() {
        return lastCompletedTask;
    }

    @Override
    public void deleteCompletedTasks(boolean completed) {
        super.deleteCompletedTasks(completed);
        this.lastDeletedCompletedStatus = completed;
    }

    public Boolean getLastDeletedCompletedStatus() {
        return lastDeletedCompletedStatus;
    }
}

