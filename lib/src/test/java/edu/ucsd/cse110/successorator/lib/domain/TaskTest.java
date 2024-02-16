package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class TaskTest {
    @Test
    public void testGetters() {
        var task = new Task("testing 1", 1, 0);
        assertEquals(Integer.valueOf(1), task.id());
        assertEquals("testing 1", task.getTask());
        assertEquals(0, task.sortOrder());
        assertFalse(task.isCompleted());
    }

    @Test
    public void testIsCompleted() {
        Task task = new Task("TASK", 1, 0);
        boolean expected = false;
        boolean actual = task.isCompleted();
        assertEquals(expected, actual);
    }

    @Test
    public void testCompleteTask() {
        Task task = new Task("TASK", 1, 0);
        task.completeTask();
        boolean expected = true;
        boolean actual = task.isCompleted();
        assertEquals(expected, actual);
    }

    @Test
    public void testUncompleteTask() {
        Task task = new Task("TASK", 1, 0);
        task.completeTask();
        boolean expected = true;
        boolean actual = task.isCompleted();
        assertEquals(expected, actual);
        task.uncompleteTask();
        expected = false;
        actual = task.isCompleted();
        assertEquals(expected, actual);
    }

    @Test
    public void testWithId() {
        var task = new Task("testing 1", 2, 1);
        var expected = new Task("testing 1", 19, 1);
        var actual = task.withId(19);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithSortOrder() {
        var task = new Task("testing 1", 2, 1);
        var expected = new Task("testing 1", 2, 81);
        var actual = task.withSortOrder(81);
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        var task1 = new Task("testing 1", 1, 0);
        var task2 = new Task("testing 1", 1, 0);
        var task3 = new Task("testing 1", 2, 0);

        assertEquals(task1, task2);
        assertNotEquals(task1, task3);
    }
}