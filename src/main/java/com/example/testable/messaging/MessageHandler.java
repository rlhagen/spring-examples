package com.example.testable.messaging;

import com.example.testable.messaging.Message;

public interface MessageHandler<T extends Message> {

    boolean isSupported(Class<?> type);

    Message handle(Message t);

}