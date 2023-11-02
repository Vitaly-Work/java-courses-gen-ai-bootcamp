package org.bananalaba.datatraining.aws.testdata.application;

import static com.google.common.base.Preconditions.checkArgument;

public class TestDataGeneratorApplicationCli {

    public static void main(String[] arguments) {
        checkArgument(arguments.length == 1, "exactly one argument is required - task definition file path");
        new TestDataGeneratorApplication().run(arguments[0]);
    }

}
