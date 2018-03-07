package com.example.testable.messaging;

public interface MessagingAdapter<T> {

    void send(T t, int priority);

    void send(T t);

    void receive(T t);

    T sendAndReceive(T t);

}
