package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

public class YearlyRecurTest {
    private YearlyRecur yearlyRecur;
    private Task task1;
    private LocalDateTime now;

    @Before
    public void setUp() {
        yearlyRecur = new YearlyRecur();
        now = LocalDateTime.now();
        task1 = new Task(0, "Test 1", false, 0, null);
        yearlyRecur.addTask(task1, now);
    }

    @Test
    public void testAddTask() {
        List<Task> tasks = yearlyRecur.getTasksForDate(now);
        assertTrue("Task list should contain task1", tasks.contains(task1));
    }

    @Test
    public void testRemoveTask() {
        assertTrue("Task should be removed successfully", yearlyRecur.removeTask(task1));
        List<Task> tasks = yearlyRecur.getTasksForDate(now.plusYears(1));
        assertFalse("Task list should not contain task1 after removal", tasks.contains(task1));
    }

    @Test
    public void testCheckRecur() {
        // Test for a month later
        LocalDateTime oneYearLater = now.plusYears(1);
        List<Task> tasksNextYear = yearlyRecur.getTasksForDate(oneYearLater);
        assertTrue("Task list should contain task1 for next year", tasksNextYear.contains(task1));

        // Test for more than a month later
        LocalDateTime twoYearsLater = now.plusYears(2);
        List<Task> tasksInTwoYears = yearlyRecur.getTasksForDate(twoYearsLater);
        assertTrue("Task list should contain task1 for two months later", tasksInTwoYears.contains(task1));
    }
}