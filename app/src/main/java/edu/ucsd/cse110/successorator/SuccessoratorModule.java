package edu.ucsd.cse110.successorator;

import android.content.Context;
import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.data.db.RoomTaskRepository;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;

@Module
@InstallIn(SingletonComponent.class)
public class SuccessoratorModule {

    @Provides
    public static SuccessoratorDatabase provideDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, SuccessoratorDatabase.class, "successorator-database")
                .allowMainThreadQueries() // Consider removing this for production
                .build();
    }

    @Provides
    public static TaskRepository provideTaskRepository(SuccessoratorDatabase database) {
        return new RoomTaskRepository(database.taskDao());
    }
}
