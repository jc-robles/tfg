package com.imudataprocessor.model.service;

import com.imudataprocessor.api.configuration.pyrhonprogram.ProgramConfiguration;
import com.imudataprocessor.api.service.ExternalProcess;
import com.imudataprocessor.api.service.TestTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;


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
    public void execute(final String testTypeName, final String nameTest) throws IOException {
        this.testTypeService.findByTestName(testTypeName).ifPresent(programConfiguration -> {
            final ProcessBuilder processBuilder = this.createProcess(programConfiguration, nameTest);
            this.configureOutputProcess(processBuilder, nameTest);
            this.runProcess(processBuilder);
        });
    }

    private ProcessBuilder createProcess(final ProgramConfiguration programConfiguration, final String nameTest) {
        final ProcessBuilder processBuilder = new ProcessBuilder("python",
                this.filePythonProgramPath + "/" + programConfiguration.getNameFile(),
                this.testsNotProcessedPath + "/" + nameTest.replace("Processed", "") + ".csv");
        processBuilder.redirectErrorStream(true);
        return processBuilder;
    }

    private void configureOutputProcess(final ProcessBuilder processBuilder, final String nameTest) {
        final File file = new File(this.testsProcessedPath + "/" + nameTest + ".json");
        processBuilder.redirectOutput(file);
    }

    private void runProcess(final ProcessBuilder processBuilder) {
        try {
            final Process process = processBuilder.start();
            process.waitFor();
        } catch (final IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
