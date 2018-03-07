package com.example.testable;

import com.example.testable.messaging.Message;
import com.example.testable.messaging.MessageHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(Ordered.LOWEST_PRECEDENCE)
public class MessageCallbackListener implements MessageHandler<Message> {

    private Runnable callback;

    @Override
    public boolean isSupported(Class<?> type) {
        return true;
    }

    @Override
    public Message handle(Message t) {
        if (callback != null) {
            callback.run();
        }
        return t;
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

}
