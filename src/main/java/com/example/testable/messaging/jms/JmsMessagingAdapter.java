package com.example.testable.messaging.jms;

import com.example.testable.messaging.Message;
import com.example.testable.messaging.MessageHandler;
import com.example.testable.messaging.MessageRouter;
import com.example.testable.messaging.MessagingAdapter;
import com.example.testable.messaging.jms.JmsMessageRouter.Destination;
import com.example.testable.model.ErrorResponse;
import org.apache.activemq.command.ActiveMQTempQueue;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Session;
import java.util.List;
import java.util.Map;



@Service
public class JmsMessagingAdapter implements MessagingAdapter<Message> {
    static Logger logger = Logger.getLogger(JmsMessagingAdapter.class);

    @Autowired
    private JmsTemplate template;

    @Autowired
    private MessageRouter<Destination, Message> router;

    @Autowired
    private List<MessageHandler<?>> handlers;

    @Override
    public void send(Message t) {
        Destination destination = router.route(t);
        template.convertAndSend(destination.getName(), t);
    }

    @Override
    public synchronized void send(Message t, int priority) {
        Destination destination = router.route(t);
        template.setPriority(priority);
        template.convertAndSend(destination.getName(), t);
    }

    @Override
    @JmsListener(destination = "response", containerFactory = "myFactory")
    @JmsListener(destination = "event", containerFactory = "myFactory")
    public void receive(Message message) {
        handlers.forEach(handler -> {
            if (handler.isSupported(message.getClass())) {
                try {
                    handler.handle(message);
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        });
    }

    //ignore below for now...

    @JmsListener(destination = "synchronous", containerFactory = "myFactory")
    public void receive(@Payload Message message, @Headers Map<String, Object> headers) {
        ActiveMQTempQueue destination = (ActiveMQTempQueue) headers.get("jms_replyTo");
        for (MessageHandler<?> handler : handlers) {
            if (handler.isSupported(message.getClass())) {
                Message response = handler.handle(message);
                template.convertAndSend(destination, response);
                return;
            }
        }
        // send response if not found
        template.convertAndSend(destination, new ErrorResponse("Not Found"));
    }

    @Override
    public Message sendAndReceive(Message t) {
        Destination destination = router.route(t);
        javax.jms.Message response = template.sendAndReceive(destination.getName(), new MessageCreator() {

            @Override
            public javax.jms.Message createMessage(Session session) throws JMSException {
                return template.getMessageConverter().toMessage(t, session);
            }
        });
        try {
            return (Message) template.getMessageConverter().fromMessage(response);
        } catch (Exception e) {
            logger.error(e);
        }
        return null;

    }

}
