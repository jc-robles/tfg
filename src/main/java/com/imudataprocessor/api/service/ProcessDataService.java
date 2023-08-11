package com.imudataprocessor.api.service;

import com.imudataprocessor.api.dto.internal.InternalDataDTO;
import com.imudataprocessor.api.dto.out.processedtest.OutputDataDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProcessDataService {

    InternalDataDTO processMainTest(MultipartFile multipartFile) throws IOException;

    InternalDataDTO processSplitTest(String fileName, Integer start, Integer end) throws IOException;

    void deleteTest(String fileName) throws IOException;

    void deleteAllTest() throws IOException;

    OutputDataDTO processDataTest(String testTypeName, String nameTest) throws Exception;
}
