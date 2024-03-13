package edu.ucsd.cse110.successorator.lib.data;

import java.time.LocalDateTime;

import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class TestableInMemoryDataSource extends InMemoryDataSource {
    private LocalDateTime time;

    public TestableInMemoryDataSource(LocalDateTime initialTime) {
        this.time = initialTime;
    }

    @Override
    public void setTime(LocalDateTime dateTime) {
        this.time = dateTime;
    }

    @Override
    public Subject<LocalDateTime> getTimeSubject() {
        // Assuming SimpleSubject is a valid Subject implementation
        SimpleSubject<LocalDateTime> subject = new SimpleSubject<>();
        subject.setValue(time);
        return subject;
    }
}