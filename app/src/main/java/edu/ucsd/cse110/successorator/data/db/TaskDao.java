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

    @Transaction
    default int append(TaskEntity task) {
        return Math.toIntExact(insert(new TaskEntity(task.task, task.id, task.sortOrder)));
    }

    @Transaction
    default int prepend(TaskEntity task){
        return Math.toIntExact(insert(new TaskEntity(task.task, task.id, task.sortOrder)));
    }
}
