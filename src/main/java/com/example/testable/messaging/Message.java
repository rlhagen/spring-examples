package com.example.testable.messaging;

public interface Message {

    interface Control extends Message {}

    interface Event extends Message {}

    interface Request extends Message {}

    interface Response extends Message {}

    interface SynchronousRequest extends Message {}

}