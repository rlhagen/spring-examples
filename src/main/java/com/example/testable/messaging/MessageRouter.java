package com.example.testable.messaging;

public interface MessageRouter<T, S> {
    T route(S s);
}