package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(TaskEntity task);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<TaskEntity> tasks);

    @Query("SELECT * FROM tasks WHERE id = :id")
    TaskEntity find(int id);

    @Query("SELECT * FROM tasks ORDER BY sortOrder")
    List<TaskEntity> findAll();

    @Query("SELECT * FROM tasks WHERE id = :id")
    LiveData<TaskEntity> findAsLiveData(int id);

    @Query("SELECT * FROM tasks ORDER BY sortOrder")
    LiveData<List<TaskEntity>> findAllAsLiveData();

    @Query("SELECT COUNT(*) FROM tasks")
    int count();

    @Query("SELECT MIN(sortOrder) FROM tasks")
    int getMinsortOrder();

    @Query("SELECT MAX(sortOrder) FROM tasks")
    int getMaxsortOrder();

    @Query("SELECT MAX(sortOrder) FROM tasks WHERE completed = 0")
    int getIncompleteMaxsortOrder();

    @Query("UPDATE tasks SET sortOrder = sortOrder + :by " + "WHERE sortOrder >= :from AND sortOrder <= :to")
    void shiftSortOrder(int from, int to, int by);

    @Query("SELECT MAX(id) FROM tasks")
    int getMaxId();

    @Transaction
    default int append(TaskEntity task) {
//        var maxSortOrder = getMaxsortOrder();
//        var maxId = getMaxId();
//        var newTask = new TaskEntity(
//                task.task + " Completed", maxId + 1, maxSortOrder + 1
//        );
//        newTask.completed = true;
//        return Math.toIntExact(insert(newTask));
        if (getIncompleteMaxsortOrder() == getMaxsortOrder()) { // if no completed tasks
            var newTask = new TaskEntity(task.task + " Completed", getMaxId() + 1, getMaxsortOrder() + 1);
            newTask.completed = true;
            return Math.toIntExact(insert(newTask));
        } else {
            shiftSortOrder(getIncompleteMaxsortOrder() + 1, getMaxsortOrder(), 1);
            var newTask = new TaskEntity(task.task + " Completed", getMaxId() + 1, getIncompleteMaxsortOrder() + 1);
            newTask.completed = true;
            return Math.toIntExact(insert(newTask));
        }
    }

    @Transaction
    default int prepend(TaskEntity task) {
        shiftSortOrder(getMinsortOrder(), getMaxsortOrder(), 1);
        var maxId = getMaxId();
        var newTask = new TaskEntity(
                task.task + " Incomplete", maxId + 1, getMinsortOrder() - 1
        );
        return Math.toIntExact(insert(newTask));
    }

    @Query("DELETE FROM tasks WHERE id = :id")
    void remove(int id);
}
