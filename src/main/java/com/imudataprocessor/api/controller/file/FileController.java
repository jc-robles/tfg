package com.imudataprocessor.api.controller.file;

import com.imudataprocessor.api.dto.out.processedtest.OutputDataDTO;
import com.imudataprocessor.api.dto.out.test.DataDTO;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileController {

    ResponseEntity<DataDTO> uploadFile(MultipartFile multipartFile, Model model) throws IOException;

    ResponseEntity<DataDTO> splitFile(String fileName, Integer start, Integer end) throws IOException;

    ResponseEntity<OutputDataDTO> processFile(String testTypeName, String fileName) throws IOException;

    ResponseEntity<HttpStatus> deleteFile(String fileName) throws IOException;

    ResponseEntity<HttpStatus> deleteAllFile() throws IOException;

    ResponseEntity<FileSystemResource> downloadProcessedTest(String nameTest);

    ResponseEntity<FileSystemResource> downloadRawTest(String nameTest);

}
