package org.bananalaba.datatraining.aws.testdata;

import static org.apache.commons.lang3.Validate.notNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDataGeneratorCliDeployment {

    private final Path deploymentDirectory;

    public TestDataGeneratorCliDeployment(Path deploymentDirectory, Path jarPath) throws IOException {
        this.deploymentDirectory = notNull(deploymentDirectory, "deployment directory required");
        Files.copy(
            jarPath,
            localPath("cli.jar")
        );
    }

    public void run(String taskClassPath) throws IOException, InterruptedException {
        run(taskClassPath, Map.of());
    }

    public void run(String taskClassPath, Map<String, String> systemProperties)
            throws IOException, InterruptedException {
        log.info("deploying the JAR into " + deploymentDirectory);
        try (var taskInput = getClass().getClassLoader().getResourceAsStream(taskClassPath)) {
            Files.copy(
                taskInput,
                localPath("task.json")
            );
        }

        var javaHome = System.getenv("JAVA_HOME");
        var errorLog = new File(deploymentDirectory.toString() + File.separator + "log.txt");

        var command = new ArrayList<String>();
        command.add(String.join(File.separator, javaHome, "bin", "java"));
        systemProperties.forEach((key, value) -> command.add("-D" + key + "=" + value));
        command.addAll(List.of(
            "-jar",
            "cli.jar",
            "task.json"
        ));

        log.info("running the JAR: " + String.join(" ", command));
        var cliRun = new ProcessBuilder()
            .directory(deploymentDirectory.toFile())
            .command(command)
            .redirectError(errorLog)
            .start();

        cliRun.waitFor();

        log.info("checking for errors...");
        var errorOutput = new StringBuilder();
        try (var br = new BufferedReader(new FileReader(errorLog))) {
            String line;
            while ((line = br.readLine()) != null) {
                errorOutput.append(line).append(System.lineSeparator());
            }

            if (!errorOutput.isEmpty()) {
                throw new TestExecutionException("the CLI has failed: " + errorOutput.toString());
            }
        }
    }

    private Path localPath(String relativePath) {
        return Path.of(deploymentDirectory.toString(), relativePath);
    }

}
