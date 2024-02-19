package edu.ucsd.cse110.successorator;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.ucsd.cse110.successorator.data.db.TaskEntity;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;

import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class MainViewModelTest {
    MainViewModel viewModel;

    @Mock
    private TaskRepository mockTaskRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        viewModel = new MainViewModel(mockTaskRepository);
    }

    @Test
    public void completeTask_DelegatesToRepository() {
        Task task = new Task(null, "Test Task", false, 1);
        viewModel.completeTask(TaskEntity.fromTask(task));
        verify(mockTaskRepository).completeTask(task); // Verify that the repository's completeTask is called
    }
}
