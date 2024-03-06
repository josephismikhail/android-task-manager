package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

public class MonthlyRecurTest {
    private MonthlyRecur monthlyRecur;
    private Task task1;
    private LocalDateTime now;

    @Before
    public void setUp() {
        monthlyRecur = new MonthlyRecur();
        now = LocalDateTime.now();
        task1 = new Task(0, "Test 1", false, 0, null);
        monthlyRecur.addTask(task1, now);
    }

    @Test
    public void testAddTask() {
        List<Task> tasks = monthlyRecur.getTasksForDate(now);
        assertTrue("Task list should contain task1", tasks.contains(task1));
    }

    @Test
    public void testRemoveTask() {
        assertTrue("Task should be removed successfully", monthlyRecur.removeTask(task1));
        List<Task> tasks = monthlyRecur.getTasksForDate(now.plusMonths(1));
        assertFalse("Task list should not contain task1 after removal", tasks.contains(task1));
    }

    @Test
    public void testCheckRecur() {
        // Test for a month later
        LocalDateTime oneMonthLater = now.plusMonths(1);
        List<Task> tasksNextMonth = monthlyRecur.getTasksForDate(oneMonthLater);
        assertTrue("Task list should contain task1 for next month", tasksNextMonth.contains(task1));

        // Test for more than a month later
        LocalDateTime twoMonthsLater = now.plusMonths(2);
        List<Task> tasksInTwoMonths = monthlyRecur.getTasksForDate(twoMonthsLater);
        assertTrue("Task list should contain task1 for two months later", tasksInTwoMonths.contains(task1));
    }
}
