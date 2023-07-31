package com.imudataprocessor.model.service;

import com.imudataprocessor.api.configuration.pyrhonprogram.ProgramConfiguration;
import com.imudataprocessor.api.configuration.pyrhonprogram.PythonProgramConfiguration;
import com.imudataprocessor.api.service.ExternalProcess;
import com.imudataprocessor.api.service.FileService;
import com.imudataprocessor.api.service.InternalDataDTO;
import com.imudataprocessor.api.service.JsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ExternalProcessImpl implements ExternalProcess /*, CommandLineRunner*/ {

    @Autowired
    private JsonService jsonService;

    @Autowired
    private FileService fileService;

    @Value("${python_program_configuration}")
    private String pythonProgramConfigurationPath;

    @Value("${file-python-program}")
    private String filePythonProgramPath;

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

    @Override
    public void execute(final InternalDataDTO internalDataDTO) throws IOException {
//        String[] a = new String[]{"0.2015380859375","0.1963958740234375","0.1940155029296875"};
//        String[] b = new String[]{"0.9995574951171875","0.9622650146484375","0.9890594482421875"};
//        String[] c = new String[]{"-0..0761566162109375","-0.088714599609375","-0.047332763671875"};




        ProcessBuilder processBuilder = new ProcessBuilder("python", "src/main/resources/python/2_min_walk.py"
//                ,Arrays.toString(internalDataDTO.getAccelerometerX().toArray())
//                , Arrays.toString(internalDataDTO.getAccelerometerY().toArray())
//                , Arrays.toString(internalDataDTO.getAccelerometerZ().toArray())
        );
        processBuilder.redirectErrorStream(true);


        File fileInput = new File("input.json");
        File file = new File("output.json");
        processBuilder.redirectOutput(file);
        processBuilder.redirectInput(fileInput);

        Process process = processBuilder.start();
    }

//    @Override
//    public void run(String... args) throws Exception {
//        this.execute("");
//    }
}
