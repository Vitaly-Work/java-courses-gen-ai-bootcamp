package org.bananalaba.datatraining.aws.testdata;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

public class TestDataGeneratorApplicationCliTest extends BaseTestDataGeneratorApplicationCliTest {

    @Test
    public void shouldRunWindowedMetricsGenerationTask() throws IOException, InterruptedException {
        var deployment = deploy();
        deployment.run("test-cases/windowed-metrics-csv/task.json");

        compareFiles("test-output/user-service.csv", "test-cases/windowed-metrics-csv/user-service.csv");
        compareFiles("test-output/order-service.csv", "test-cases/windowed-metrics-csv/order-service.csv");
    }

    @Test
    public void shouldRunServerAccessLogGenerationTask() throws IOException, InterruptedException {
        var deployment = new TestDataGeneratorCliDeployment(
            testDirectory, Path.of(System.getProperty("cliJarPath")));
        deployment.run("test-cases/server-access-log-txt/task.json");

        compareFiles(
            "test-output/user-service-2022-09-15.txt",
            "test-cases/server-access-log-txt/user-service-2022-09-15.txt"
        );
        compareFiles(
            "test-output/user-service-2022-09-16.txt",
            "test-cases/server-access-log-txt/user-service-2022-09-16.txt"
        );

        compareFiles(
            "test-output/order-service-2022-09-15.txt",
            "test-cases/server-access-log-txt/order-service-2022-09-15.txt"
        );
        compareFiles(
            "test-output/order-service-2022-09-16.txt",
            "test-cases/server-access-log-txt/order-service-2022-09-16.txt"
        );

        compareFiles(
            "test-output/payment-service-2022-09-15.txt",
            "test-cases/server-access-log-txt/payment-service-2022-09-15.txt"
        );
        compareFiles(
            "test-output/payment-service-2022-09-16.txt",
            "test-cases/server-access-log-txt/payment-service-2022-09-16.txt"
        );

        compareFiles(
            "test-output/audit-service-2022-09-15.txt",
            "test-cases/server-access-log-txt/audit-service-2022-09-15.txt"
        );
        compareFiles(
            "test-output/audit-service-2022-09-16.txt",
            "test-cases/server-access-log-txt/audit-service-2022-09-16.txt"
        );
    }

    private void compareFiles(String actualPath, String expectedPath) throws IOException {
        try (
                var actual = new FileInputStream(testPath(actualPath).toFile());
                var expected = readClassPath(expectedPath)) {
            assertThat(actual).describedAs(actualPath + " has unexpected content").hasSameContentAs(expected);
        }
    }

}
