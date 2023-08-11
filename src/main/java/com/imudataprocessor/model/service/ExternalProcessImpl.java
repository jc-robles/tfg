package com.imudataprocessor.model.service;

import com.imudataprocessor.api.configuration.pyrhonprogram.ProgramConfiguration;
import com.imudataprocessor.api.service.ExternalProcess;
import com.imudataprocessor.api.service.TestTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;


@Service
public class ExternalProcessImpl implements ExternalProcess {

    @Autowired
    private TestTypeService testTypeService;

    @Value("${file-python-program}")
    private String filePythonProgramPath;

    @Value("${split-tests-not-processed-path}")
    private String testsNotProcessedPath;

    @Value("${tests-processed-path}")
    private String testsProcessedPath;

    @Override
    public void execute(final String testTypeName, final String nameTest) throws Exception {
        final Optional<ProgramConfiguration> programConfiguration = this.testTypeService.findByTestName(testTypeName);
        if (programConfiguration.isPresent()) {
            final ProcessBuilder processBuilder = this.createProcess(programConfiguration.get(), nameTest);
            this.configureOutputProcess(processBuilder, nameTest);
            this.runProcess(processBuilder);
        }
    }

    private ProcessBuilder createProcess(final ProgramConfiguration programConfiguration, final String nameTest) {
        final ProcessBuilder processBuilder = new ProcessBuilder("python",
                this.filePythonProgramPath + "/" + programConfiguration.getNameFile(),
                this.testsNotProcessedPath + "/" + nameTest.replace("Processed", "") + ".csv");
        processBuilder.redirectErrorStream(true);
        return processBuilder;
    }

    private void configureOutputProcess(final ProcessBuilder processBuilder, final String nameTest) throws IOException {
        final Path path = Paths.get(Objects.requireNonNull(this.testsProcessedPath));
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
        final File file = new File(this.testsProcessedPath + "/" + nameTest + ".json");
        processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(file));
    }

    private void runProcess(final ProcessBuilder processBuilder) throws Exception {
        final Process process = processBuilder.start();
        final int exit = process.waitFor();
        process.getOutputStream().close();

        if (exit != 0) {
            throw new Exception("Python process execution failed");
        }
    }
}
