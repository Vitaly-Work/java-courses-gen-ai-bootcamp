package org.bananalaba.datatraining.aws.testdata.application;

public class TestDataGeneratorExecutionException extends RuntimeException {

    public TestDataGeneratorExecutionException(String message) {
        super(message);
    }

    public TestDataGeneratorExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

}
