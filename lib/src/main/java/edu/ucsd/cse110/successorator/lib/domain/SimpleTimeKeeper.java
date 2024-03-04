package edu.ucsd.cse110.successorator.lib.domain;

import java.time.LocalDateTime;

public class SimpleTimeKeeper implements TimeKeeper {
    private LocalDateTime currentTime;

    public SimpleTimeKeeper() {
        currentTime = LocalDateTime.now();
    }

    @Override
    public LocalDateTime getDateTime() {
        return currentTime;
    }

    @Override
    public void setDateTime(LocalDateTime dateTime) {
        currentTime = dateTime;
    }
}
