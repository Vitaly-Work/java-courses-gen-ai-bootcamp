package org.bananalaba.datatraining.aws.testdata.event.sink;

public class TestDataSinkException extends RuntimeException {

    public TestDataSinkException(String message) {
        super(message);
    }

    public TestDataSinkException(String message, Throwable cause) {
        super(message, cause);
    }

}
