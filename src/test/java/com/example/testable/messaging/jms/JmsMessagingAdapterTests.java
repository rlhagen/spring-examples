package com.example.testable.messaging.jms;

import com.example.testable.MessageCallbackListener;
import com.example.testable.messaging.Message;
import com.example.testable.messaging.MessagingAdapter;
import com.example.testable.model.KeepAliveEvent;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JmsMessagingAdapterTests {

    @Autowired
    MessagingAdapter<Message> adapter;

    @Autowired
    MessageCallbackListener handler;


    @Test
    public void test() throws Exception {

        CountDownLatch latch = new CountDownLatch(1);

        handler.setCallback(() -> {
            System.out.println("Callback reached!");
            //check database, etc.
            latch.countDown();

        });

        adapter.send(new KeepAliveEvent());

        latch.await(100, TimeUnit.MILLISECONDS);

        Assert.assertEquals(0, latch.getCount());
    }

}


