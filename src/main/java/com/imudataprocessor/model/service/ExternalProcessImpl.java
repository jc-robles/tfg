package com.imudataprocessor.model.service;

import com.imudataprocessor.api.configuration.pyrhonprogram.ProgramConfiguration;
import com.imudataprocessor.api.configuration.pyrhonprogram.PythonProgramConfiguration;
import com.imudataprocessor.api.service.ExternalProcess;
import com.imudataprocessor.api.service.FileService;
import com.imudataprocessor.api.service.JsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ExternalProcessImpl implements ExternalProcess {

    @Autowired
    private JsonService jsonService;

    @Autowired
    private FileService fileService;

    @Value("${python_program_configuration}")
    private String pythonProgramConfigurationPath;

    @Value("${file-python-program}")
    private String filePythonProgramPath;

    @Value("${tests-not-processed-path}")
    private String testsNotProcessedPath;

    @Value("${tests-processed-path}")
    private String testsProcessedPath;

    @Override
    public void createNewProgramToExecute(final ProgramConfiguration programConfiguration, final byte[] programFile) throws IOException {
        PythonProgramConfiguration pythonProgramConfiguration =
                (PythonProgramConfiguration) this.jsonService.readFile(this.pythonProgramConfigurationPath, PythonProgramConfiguration.class);
        pythonProgramConfiguration.getProgramConfigurations().add(programConfiguration);
        this.jsonService.saveFile(this.pythonProgramConfigurationPath, pythonProgramConfiguration);
        fileService.save(this.filePythonProgramPath, programConfiguration.getNameFile(), programFile);
    }

    public List<String> getAllNameTest() throws IOException {
        PythonProgramConfiguration pythonProgramConfiguration =
                (PythonProgramConfiguration) this.jsonService.readFile(this.pythonProgramConfigurationPath, PythonProgramConfiguration.class);
        return pythonProgramConfiguration.getProgramConfigurations().stream().map(ProgramConfiguration::getNameTest).collect(Collectors.toList());
    }

    public Optional<ProgramConfiguration> findByTestName(final String testName) throws IOException {
        PythonProgramConfiguration pythonProgramConfiguration =
                (PythonProgramConfiguration) this.jsonService.readFile(this.pythonProgramConfigurationPath, PythonProgramConfiguration.class);
        return pythonProgramConfiguration.getProgramConfigurations().stream()
                .filter(programConfiguration -> ObjectUtils.nullSafeEquals(programConfiguration.getNameTest(), testName))
                .findFirst();
    }

    @Override
    public void execute(final String testTypeName, final String nameTest) throws IOException {
        this.findByTestName(testTypeName).ifPresent(programConfiguration -> {
            ProcessBuilder processBuilder = new ProcessBuilder("python",
                    this.filePythonProgramPath + "/" + programConfiguration.getNameFile(),
                    this.testsNotProcessedPath + "/" + nameTest);
            processBuilder.redirectErrorStream(true);

            File file = new File(this.testsProcessedPath + nameTest.replace(".csv$",".json"));
            processBuilder.redirectOutput(file);

            try {
                Process process = processBuilder.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
