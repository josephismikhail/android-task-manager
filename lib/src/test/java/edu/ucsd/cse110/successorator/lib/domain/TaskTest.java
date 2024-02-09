package edu.ucsd.cse110.successorator.lib.domain;

import junit.framework.TestCase;

public class TaskTest extends TestCase {

    public void testGetTask() {
        Task task = new Task("EX Task", 1);
        String expected = "EX Task";
        String actual = task.getTask();
        assertEquals(expected, actual);
    }

    public void testIsCompleted() {
        Task task = new Task("TASK", 1);
        boolean expected = false;
        boolean actual = task.isCompleted();
        assertEquals(expected, actual);
    }

    public void testCompleteTask() {
        Task task = new Task("TASK", 1);
        task.completeTask();
        boolean expected = true;
        boolean actual = task.isCompleted();
        assertEquals(expected, actual);
    }

    public void testUncompleteTask() {
        Task task = new Task("TASK", 1);
        task.completeTask();
        boolean expected = true;
        boolean actual = task.isCompleted();
        assertEquals(expected, actual);
        task.uncompleteTask();
        expected = false;
        actual = task.isCompleted();
        assertEquals(expected, actual);
    }
}