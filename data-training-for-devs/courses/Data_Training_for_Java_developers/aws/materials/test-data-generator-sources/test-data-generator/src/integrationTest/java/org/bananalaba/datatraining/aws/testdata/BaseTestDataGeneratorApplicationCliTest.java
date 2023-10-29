package org.bananalaba.datatraining.aws.testdata;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import org.junit.jupiter.api.io.TempDir;

public abstract class BaseTestDataGeneratorApplicationCliTest {

    @TempDir
    Path testDirectory;

    TestDataGeneratorCliDeployment deploy() throws IOException {
        return new TestDataGeneratorCliDeployment(testDirectory, Path.of(System.getProperty("cliJarPath")));
    }

    InputStream readClassPath(String path) {
        return getClass().getClassLoader().getResourceAsStream(path);
    }

    Path testPath(String relativePath) {
        return Path.of(testDirectory.toString(), relativePath);
    }

}
