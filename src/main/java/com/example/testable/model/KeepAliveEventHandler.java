package com.example.testable.model;

import com.example.testable.messaging.Message;
import com.example.testable.messaging.MessageHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(Ordered.HIGHEST_PRECEDENCE)
public class KeepAliveEventHandler implements MessageHandler<KeepAliveEvent>{

    //inject Repository, etc.

    @Override
    public boolean isSupported(Class<?> type) {
        return type.isAssignableFrom(KeepAliveEvent.class);
    }

    @Override
    public Message handle(Message t) {
        System.out.println(t);
        return null;
    }

}
