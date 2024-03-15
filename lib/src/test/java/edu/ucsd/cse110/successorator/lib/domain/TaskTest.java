package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class TaskTest {
    @Test
    public void testGetters() {
        var task = new Task(1, "testing 1", true, 0, System.currentTimeMillis(), TaskContext.HOME, RecurType.ONCE, null, true);
        assertEquals(Integer.valueOf(1), task.getId());
        assertEquals("testing 1", task.getTask());
        assertEquals(0, task.getSortOrder());
        assertTrue(task.isCompleted());
    }

    @Test
    public void testWithId() {
        var task = new Task(2, "testing 1", true, 1, System.currentTimeMillis(), TaskContext.HOME, RecurType.ONCE, null, true);
        var expected = new Task(19, "testing 1", true, 1, System.currentTimeMillis(), TaskContext.HOME, RecurType.ONCE, null, true);
        var actual = task.withId(19);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithSortOrder() {
        var task = new Task(2, "testing 1", true, 1, System.currentTimeMillis(), TaskContext.HOME, RecurType.ONCE, null, true);
        var expected = task;
        assertEquals(task, expected);
        assertNotEquals(expected, task.withSortOrder(81));
    }

    @Test
    public void testEquals() {
        var task1 = new Task(1, "testing 1", true, 0, System.currentTimeMillis(), TaskContext.HOME, RecurType.ONCE, null, true);
        var task2 = new Task(1, "testing 1", true, 0, System.currentTimeMillis(), TaskContext.HOME, RecurType.ONCE, null, true);
        var task3 = new Task(2, "testing 1", true, 0, System.currentTimeMillis(), TaskContext.HOME, RecurType.ONCE, null, true);

        assertEquals(task1, task2);
        assertNotEquals(task1, task3);
    }

    @Test
    public void testChangeStatus() {
        var task1 = new Task(1, "testing 1", false, 0, System.currentTimeMillis(), TaskContext.HOME, RecurType.ONCE, null, true);

        assertFalse(task1.isCompleted());
        task1.changeStatus();
        assertTrue(task1.isCompleted());
        task1.changeStatus();
        assertFalse(task1.isCompleted());
    }


    @Test
    public void testGetCompletedTime() {
        Long now = System.currentTimeMillis();
        var task = new Task(3, "testing 2", true, 2, now, TaskContext.HOME, RecurType.ONCE, null, false);
        assertEquals(now, task.getCompletedTime());
    }

    @Test
    public void testGetRecurType() {
        var task = new Task(3, "testing 2", true, 2, null, TaskContext.HOME, RecurType.DAILY, null, false);
        assertEquals(RecurType.DAILY, task.getRecurType());
    }

    @Test
    public void testGetRecurDate() {
        Long futureDate = System.currentTimeMillis() + 100000;
        var task = new Task(3, "testing 3", false, 2, null, TaskContext.HOME, RecurType.WEEKLY, futureDate, false);
        assertEquals(futureDate, task.getRecurDate());
    }

    @Test
    public void testDisplay() {
        var task = new Task(3, "testing 4", false, 2, null, TaskContext.HOME, RecurType.MONTHLY, null, true);
        assertTrue(task.display());
    }

    @Test
    public void testWithRecurType() {
        var task = new Task(4, "testing 5", false, 3, null, TaskContext.HOME, null, null, true);
        var expected = new Task(4, "testing 5", false, 3, null, TaskContext.HOME, RecurType.YEARLY, null, true);
        var actual = task.withRecurType(RecurType.YEARLY);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithRecurDate() {
        var task = new Task(5, "testing 6", false, 4, null, TaskContext.HOME, null, null, true);
        Long newRecurDate = System.currentTimeMillis() + 50000;
        var expected = new Task(5, "testing 6", false, 4, null, TaskContext.HOME, null, newRecurDate, true);
        var actual = task.withRecurDate(newRecurDate);
        assertEquals(expected, actual);
    }

    @Test
    public void testUncomplete() {
        var task = new Task(6, "testing 7", true, 5, System.currentTimeMillis(), TaskContext.HOME, null, null, true);
        task.uncomplete();
        assertFalse(task.isCompleted());
    }

    @Test
    public void testSetDisplay() {
        var task = new Task(7, "testing 8", false, 6, null, TaskContext.HOME, null, null, false);
        assertFalse(task.display());
        task.setDisplay(true);
        assertTrue(task.display());
    }

}