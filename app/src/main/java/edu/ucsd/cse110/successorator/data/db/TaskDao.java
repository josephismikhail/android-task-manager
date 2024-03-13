package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.Context;
import edu.ucsd.cse110.successorator.lib.domain.RecurType;

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

    @Query("SELECT MAX(id) FROM tasks")
    int getMaxId();

    @Query("SELECT MIN(sortOrder) FROM tasks")
    int getMinSortOrder();

    @Query("SELECT MAX(sortOrder) FROM tasks")
    int getMaxSortOrder();

    @Query("SELECT MAX(sortOrder) FROM tasks WHERE completed = 0")
    int getIncompleteMaxSortOrder();

    @Query("UPDATE tasks SET sortOrder = sortOrder + :by " + "WHERE sortOrder >= :from AND sortOrder <= :to")
    void shiftSortOrder(int from, int to, int by);

    @Transaction
    default int prepend(TaskEntity task) {
        shiftSortOrder(getMinSortOrder(), getMaxSortOrder(), 1);
        var newTask = new TaskEntity(null, task.task, false, getMinSortOrder() - 1,
                null, Context.HOME, RecurType.ONCE, null, task.display
        );
        return Math.toIntExact(insert(newTask));
    }

    @Query("DELETE FROM tasks WHERE id = :id")
    void remove(int id);

    @Query("DELETE FROM tasks WHERE completed = 1 AND completedTime < :cutoffTime")
    void deleteCompletedTasksBefore(long cutoffTime);
}
