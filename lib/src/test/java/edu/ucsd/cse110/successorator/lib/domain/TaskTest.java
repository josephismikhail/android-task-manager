package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class TaskTest {
    @Test
    public void testGetters() {
        var task = new Task(1, "testing 1", true, 0, System.currentTimeMillis());
        assertEquals(Integer.valueOf(1), task.getId());
        assertEquals("testing 1", task.getTask());
        assertEquals(0, task.getSortOrder());
        assertTrue(task.isCompleted());
    }

    @Test
    public void testWithId() {
        var task = new Task(2, "testing 1", true, 1, System.currentTimeMillis());
        var expected = new Task(19, "testing 1", true, 1, System.currentTimeMillis());
        var actual = task.withId(19);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithSortOrder() {
        var task = new Task(2, "testing 1", true, 1, System.currentTimeMillis());
        var expected = new Task(2, "testing 1", true, 81, System.currentTimeMillis());
        var actual = task.withSortOrder(81);
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        var task1 = new Task(1, "testing 1", true, 0, System.currentTimeMillis());
        var task2 = new Task(1, "testing 1", true, 0, System.currentTimeMillis());
        var task3 = new Task(2, "testing 1", true, 0, System.currentTimeMillis());

        assertEquals(task1, task2);
        assertNotEquals(task1, task3);
    }

    @Test
    public void testChangeStatus() {
        var task1 = new Task(1, "testing 1", false, 0, System.currentTimeMillis());

        assertFalse(task1.isCompleted());
        task1.changeStatus();
        assertTrue(task1.isCompleted());
        task1.changeStatus();
        assertFalse(task1.isCompleted());
    }
}