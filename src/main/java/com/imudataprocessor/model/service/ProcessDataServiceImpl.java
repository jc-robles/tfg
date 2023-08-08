package com.imudataprocessor.model.service;

import com.imudataprocessor.api.configuration.pyrhonprogram.ProgramConfiguration;
import com.imudataprocessor.api.dto.internal.InternalDataDTO;
import com.imudataprocessor.api.dto.out.processedtest.OutputDataDTO;
import com.imudataprocessor.api.service.ExternalProcess;
import com.imudataprocessor.api.service.FileService;
import com.imudataprocessor.api.service.ProcessDataService;
import com.imudataprocessor.api.service.TestTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ProcessDataServiceImpl implements ProcessDataService {

    @Autowired
    private FileService fileService;

    @Autowired
    private ExternalProcess externalProcess;

    @Autowired
    private TestTypeService testTypeService;

    @Value("${main-file-path}")
    private String mainFilePath;

    @Override
    public InternalDataDTO processMainTest(final MultipartFile multipartFile) throws IOException {
        this.fileService.save(this.mainFilePath, multipartFile.getOriginalFilename(), multipartFile.getBytes());
        return this.fileService.getDataFromMainFile();
    }

    @Override
    public InternalDataDTO processSplitTest(final String fileName, final Integer start, final Integer end) throws IOException {
        this.fileService.split(fileName, start, end);
        return this.fileService.getDataFromTest(fileName);
    }

    @Override
    public void deleteTest(final String fileName) throws IOException {
        this.fileService.deleteTest(fileName);
    }

    @Override
    public void deleteAllTest() throws IOException {
        this.fileService.deleteAllTest();
    }

    @Override
    public OutputDataDTO processDataTest(final String testTypeName, final String nameTest) throws IOException {
        this.externalProcess.execute(testTypeName, nameTest);
        final Optional<ProgramConfiguration> programConfiguration = this.testTypeService.findByTestName(testTypeName);
        return this.fileService.obtainDataToFileProcessed(programConfiguration, nameTest);
    }

}
