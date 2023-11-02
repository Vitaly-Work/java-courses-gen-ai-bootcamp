package org.bananalaba.datatraining.aws.testdata.event.time;

public class EventTimingException extends RuntimeException {

    public EventTimingException(String message) {
        super(message);
    }

    public EventTimingException(String message, Throwable cause) {
        super(message, cause);
    }

}
