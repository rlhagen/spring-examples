package com.example.testable.model;

import com.example.testable.messaging.Message.Event;

public class KeepAliveEvent implements Event{
    private static long id = 0;

    public long getId(){
        return id += 1;
    }

}
