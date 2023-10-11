package org.bananalaba.datatraining.aws.testdata.event.sink;

public class EventSinkException extends RuntimeException {

    public EventSinkException(String message) {
        super(message);
    }

    public EventSinkException(String message, Throwable cause) {
        super(message, cause);
    }

}
