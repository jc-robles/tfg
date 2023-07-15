package com.imudataprocessor.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileController {
    ResponseEntity<DataDTO> uploadFile(MultipartFile multipartFile, Model model) throws IOException;

    ResponseEntity<DataDTO> splitFile(String fileName, Integer start, Integer end) throws IOException;

    ResponseEntity<DataDTO> processFile(String fileName) throws IOException;

    ResponseEntity<HttpStatus> deleteFile(String fileName) throws IOException;

    ResponseEntity<HttpStatus> deleteAllFile() throws IOException;

}
