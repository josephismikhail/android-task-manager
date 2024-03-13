package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;
import org.junit.Test;
import java.time.LocalDateTime;

import edu.ucsd.cse110.successorator.lib.data.TestableInMemoryDataSource;

public class SimpleTimeKeeperTest {

    @Test
    public void testSetDateTime() {
        LocalDateTime testTime = LocalDateTime.now();
        TestableInMemoryDataSource dataSource = new TestableInMemoryDataSource(testTime);
        SimpleTimeKeeper timeKeeper = new SimpleTimeKeeper(dataSource);

        // Test initial state
        assertEquals(testTime, timeKeeper.getDateTime().getValue());

        // Change the time
        LocalDateTime newTime = testTime.plusDays(1);
        timeKeeper.setDateTime(newTime);

        // Test updated state
        assertEquals(newTime, timeKeeper.getDateTime().getValue());
    }

    @Test
    public void testGetDateTime() {
        LocalDateTime testTime = LocalDateTime.now();
        TestableInMemoryDataSource dataSource = new TestableInMemoryDataSource(testTime);
        SimpleTimeKeeper timeKeeper = new SimpleTimeKeeper(dataSource);

        // Test that getDateTime retrieves the correct initial time
        assertEquals(testTime, timeKeeper.getDateTime().getValue());

        // Change the time in the data source directly
        LocalDateTime newTime = testTime.plusHours(5);
        dataSource.setTime(newTime);

        // Test that getDateTime retrieves the updated time
        assertEquals(newTime, timeKeeper.getDateTime().getValue());
    }
}
