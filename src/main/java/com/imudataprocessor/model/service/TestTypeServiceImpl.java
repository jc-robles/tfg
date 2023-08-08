package com.imudataprocessor.model.service;

import com.imudataprocessor.api.configuration.pyrhonprogram.ProgramConfiguration;
import com.imudataprocessor.api.configuration.pyrhonprogram.PythonProgramConfiguration;
import com.imudataprocessor.api.dto.out.createtest.CreateTestDTO;
import com.imudataprocessor.api.service.FileService;
import com.imudataprocessor.api.service.JsonService;
import com.imudataprocessor.api.service.TestTypeService;
import com.imudataprocessor.model.mapper.ProgramConfigurationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TestTypeServiceImpl implements TestTypeService {

    @Autowired
    private JsonService jsonService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ProgramConfigurationMapper programConfigurationMapper;

    @Value("${file-python-program}")
    private String filePythonProgramPath;

    @Value("${python_program_configuration}")
    private String pythonProgramConfigurationPath;

    @Override
    public List<String> getAllNameTest() throws IOException {
        final PythonProgramConfiguration pythonProgramConfiguration =
                (PythonProgramConfiguration) this.jsonService.readFile(this.pythonProgramConfigurationPath, PythonProgramConfiguration.class);
        return pythonProgramConfiguration.getProgramConfigurations().stream().map(ProgramConfiguration::getNameTest).collect(Collectors.toList());
    }

    @Override
    public Optional<ProgramConfiguration> findByTestName(final String testName) throws IOException {
        final PythonProgramConfiguration pythonProgramConfiguration =
                (PythonProgramConfiguration) this.jsonService.readFile(this.pythonProgramConfigurationPath, PythonProgramConfiguration.class);
        return pythonProgramConfiguration.getProgramConfigurations().stream()
                .filter(programConfiguration -> ObjectUtils.nullSafeEquals(programConfiguration.getNameTest(), testName))
                .findFirst();
    }

    @Override
    public void createTestType(final String createTest, final MultipartFile fileTest) throws IOException {
        final CreateTestDTO createTestDTO = (CreateTestDTO) this.jsonService.convertToObject(createTest, CreateTestDTO.class);
        final ProgramConfiguration programConfiguration = this.programConfigurationMapper.map(createTestDTO);
        programConfiguration.setNameFile(fileTest.getOriginalFilename());

        final PythonProgramConfiguration pythonProgramConfiguration =
                (PythonProgramConfiguration) this.jsonService.readFile(this.pythonProgramConfigurationPath, PythonProgramConfiguration.class);
        pythonProgramConfiguration.getProgramConfigurations().add(programConfiguration);
        this.jsonService.saveFile(this.pythonProgramConfigurationPath, pythonProgramConfiguration);
        this.fileService.save(this.filePythonProgramPath, programConfiguration.getNameFile(), fileTest.getBytes());
    }

    @Override
    public void removeTestType(final String testType) throws IOException {
        final PythonProgramConfiguration pythonProgramConfiguration =
                (PythonProgramConfiguration) this.jsonService.readFile(this.pythonProgramConfigurationPath, PythonProgramConfiguration.class);
        final Optional<ProgramConfiguration> programConfigurationOptional = pythonProgramConfiguration.getProgramConfigurations().stream()
                .filter(programConfiguration -> ObjectUtils.nullSafeEquals(programConfiguration.getNameTest(), testType))
                .findFirst();

        if (programConfigurationOptional.isPresent()) {
            pythonProgramConfiguration.getProgramConfigurations().remove(programConfigurationOptional.get());
            this.jsonService.saveFile(this.pythonProgramConfigurationPath, pythonProgramConfiguration);
            this.fileService.deleteTestType(programConfigurationOptional.get().getNameFile());
        }
    }

}
