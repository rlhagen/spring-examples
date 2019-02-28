package com.example.testable.messaging.jms;

import com.example.testable.messaging.Message;
import com.example.testable.messaging.MessageRouter;

public class JmsMessageRouter implements MessageRouter<JmsMessageRouter.Destination, Message> {

    /**
     *
     * control is a TOPIC, all others are QUEUES
     *
     */
    public enum Destination {
        CONTROL("control", true), EVENT("event", false), REQUEST("request", false), RESPONSE("response", false), SYNCHRONOUS(
                "synchronous", false);

        String name;
        boolean topic;

        Destination(String name, boolean topic) {
            this.name = name;
            this.topic = topic;
        }

        public String getName() {
            return name;
        }

        public boolean isTopic() {
            return topic;
        }

    }

    @Override
    public Destination route(Message m) {
        if (m instanceof Message.Control)
            return Destination.CONTROL;
        if (m instanceof Message.Event)
            return Destination.EVENT;
        if (m instanceof Message.Request)
            return Destination.REQUEST;
        if (m instanceof Message.Response)
            return Destination.RESPONSE;
        if (m instanceof Message.SynchronousRequest)
            return Destination.SYNCHRONOUS;
        throw new UnsupportedOperationException();
    }

}
