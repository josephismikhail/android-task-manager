package edu.ucsd.cse110.successorator.lib.domain;

import java.time.LocalDateTime;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class SimpleTimeKeeper implements TimeKeeper {
    private final InMemoryDataSource dataSource;

    public SimpleTimeKeeper(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Subject<LocalDateTime> getDateTime() {
        return dataSource.getTimeSubject();
    }

    @Override
    public void setDateTime(LocalDateTime dateTime) {
        dataSource.setTime(dateTime);
    }
}
