package com.imudataprocessor.api.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProcessDataService {

    InternalDataDTO processMainTest(MultipartFile multipartFile) throws IOException;

    InternalDataDTO processSplitTest(String fileName, Integer start, Integer end) throws IOException;

    void deleteTest(String fileName) throws IOException;

    void deleteAllTest() throws IOException;

    InternalDataDTO processDataTest(String testTypeName, String nameTest) throws IOException;
}
