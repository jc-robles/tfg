package com.imudataprocessor.api.service;

import com.imudataprocessor.api.configuration.pyrhonprogram.ProgramConfiguration;
import com.imudataprocessor.api.dto.internal.InternalDataDTO;
import com.imudataprocessor.api.dto.out.processedtest.OutputDataDTO;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public interface FileService {

    void save(String path, String fileName, byte[] bytes) throws IOException;

    File split(String fileName, Integer start, Integer end) throws IOException;

    InternalDataDTO getDataFromMainFile() throws IOException;

    InternalDataDTO getDataFromTest(String nameTest) throws IOException;

    InternalDataDTO getDataFromProcessedTest(String nameTest) throws IOException;

    OutputDataDTO obtainDataToFileProcessed(Optional<ProgramConfiguration> programConfiguration, String nameTest) throws IOException;

    void deleteTest(String nameTest) throws IOException;

    void deleteAllTest() throws IOException;

}
