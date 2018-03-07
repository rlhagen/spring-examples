package com.example.testable.messaging.jms;

import com.example.testable.messaging.Message;
import com.example.testable.messaging.MessageRouter;
import com.example.testable.messaging.jms.JmsMessageRouter.Destination;
import com.example.testable.model.KeepAliveEvent;
import org.junit.Assert;
import org.junit.Test;

public class JmsMessageRouterTests {

    MessageRouter<Destination, Message> router = new JmsMessageRouter();

    @Test
    public void routeEvent(){
        Destination destination = router.route(new KeepAliveEvent());
        Assert.assertEquals(Destination.EVENT, destination);
    }

}
