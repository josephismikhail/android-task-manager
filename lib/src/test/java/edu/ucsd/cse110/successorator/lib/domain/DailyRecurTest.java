package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.recur.DailyRecur;

public class DailyRecurTest {
    private DailyRecur dailyRecur;
    private Task task1;

    @Before
    public void setUp() {
        dailyRecur = new DailyRecur(DayOfWeek.MONDAY);
        task1 = new Task(0, "Test 1", false, 0, null);
        dailyRecur.addTask(task1, LocalDateTime.now());
    }

    @Test
    public void testAddTask() {
        List<Task> tasks = dailyRecur.getTasksForDate(LocalDateTime.now());
        assertTrue("Task list should contain task1", tasks.contains(task1));
    }

    @Test
    public void testRemoveTask() {
        assertTrue("Task should be removed successfully", dailyRecur.removeTask(task1));
        List<Task> tasks = dailyRecur.getTasksForDate(LocalDateTime.now());
        assertFalse("Task list should not contain task1 after removal", tasks.contains(task1));
    }

    @Test
    public void testCheckRecur() {
        // Assuming you renamed checkRecur to getTasksForDate
        List<Task> tasks = dailyRecur.getTasksForDate(LocalDateTime.now());
        assertEquals("There should be one task in the list", 1, tasks.size());
        assertEquals("The task in the list should be task1", task1, tasks.get(0));
    }
    @Test
    public void testGetTasksForDate() {
        Task task2 = new Task(1, "Test 2", false, 1, null);
        dailyRecur.addTask(task2, LocalDateTime.now().plusDays(1));

        // Execution & Verification
        List<Task> tasksForToday = dailyRecur.getTasksForDate(LocalDateTime.now());
        assertEquals(2, tasksForToday.size());
        assertTrue(tasksForToday.contains(task1));
        assertTrue(tasksForToday.contains(task2));

        // Test with a future date
        List<Task> tasksForTomorrow = dailyRecur.getTasksForDate(LocalDateTime.now().plusDays(1));
        assertEquals(2, tasksForTomorrow.size());
        assertTrue(tasksForTomorrow.contains(task1));
        assertTrue(tasksForTomorrow.contains(task2));
    }
}