package com.example.finalproject;

public class ScheduleItem {
    private final String time;
    private final String subject;
    private final String room;

    public ScheduleItem(String time, String subject, String room) {
        this.time = time;
        this.subject = subject;
        this.room = room;
    }

    public String getTime() { return time; }
    public String getSubject() { return subject; }
    public String getRoom() { return room; }
}

