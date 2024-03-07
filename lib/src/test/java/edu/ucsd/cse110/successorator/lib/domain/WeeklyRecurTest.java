package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.recur.WeeklyRecur;

public class WeeklyRecurTest {
    private WeeklyRecur weeklyRecur;
    private Task task1;
    private LocalDateTime now;

    @Before
    public void setUp() {
        weeklyRecur = new WeeklyRecur();
        now = LocalDateTime.now();
        task1 = new Task(0, "Test 1", false, 0, null);
        weeklyRecur.addTask(task1, now);
    }

    @Test
    public void testAddTask() {
        List<Task> tasks = weeklyRecur.getTasksForDate(now);
        assertTrue("Task list should contain task1", tasks.contains(task1));
    }

    @Test
    public void testRemoveTask() {
        assertTrue("Task should be removed successfully", weeklyRecur.removeTask(task1));
        List<Task> tasks = weeklyRecur.getTasksForDate(now.plusWeeks(1));
        assertFalse("Task list should not contain task1 after removal", tasks.contains(task1));
    }

    @Test
    public void testCheckRecur() {
        // Test for a week later
        LocalDateTime oneWeekLater = now.plusWeeks(1);
        List<Task> tasksNextWeek = weeklyRecur.getTasksForDate(oneWeekLater);
        assertTrue("Task list should contain task1 for next week", tasksNextWeek.contains(task1));

        // Test for more than a week later
        LocalDateTime twoWeeksLater = now.plusWeeks(2);
        List<Task> tasksInTwoWeeks = weeklyRecur.getTasksForDate(twoWeeksLater);
        assertTrue("Task list should contain task1 for two weeks later", tasksInTwoWeeks.contains(task1));
    }
}