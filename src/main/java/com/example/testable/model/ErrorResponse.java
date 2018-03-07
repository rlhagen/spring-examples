package com.example.testable.model;

import com.example.testable.messaging.Message;

public class ErrorResponse implements Message.Response{
    private String details;

    public ErrorResponse(String details) {
        this.details = details;
    }

    public String getDetails(){
        return details;
    }

}
