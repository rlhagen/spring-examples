package com.example.testable.messaging;


public interface MessageHandler<T extends Message> {

    boolean isSupported(Class<?> type);

    Message handle(Message t);

}