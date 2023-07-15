package com.imudataprocessor.api.service;

import com.imudataprocessor.api.controller.DataDTO;

import java.io.File;
import java.io.IOException;

public interface FileService {

    void save(String fileName, byte[] bytes) throws IOException;

    File split(String fileName, Integer start, Integer end) throws IOException;

    InternalDataDTO getDataFromMainFile() throws IOException;

    InternalDataDTO getDataFromTest(String nameTest) throws IOException;

    InternalDataDTO getDataFromProcessedTest(String nameTest) throws IOException;

    void deleteTest(String nameTest) throws IOException;

    void deleteAllTest() throws IOException;

}
