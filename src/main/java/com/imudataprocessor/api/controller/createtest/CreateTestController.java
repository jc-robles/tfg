package com.imudataprocessor.api.controller.createtest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CreateTestController {

    ResponseEntity<HttpStatus> createTest(Model model, String createTest, MultipartFile fileTest) throws IOException;

    String addOutputData(Model model, String dataName) throws IOException;

    String addGrouping(Model model, String groupingName) throws IOException;

    String getAllGrouping(Model model) throws IOException;

    ResponseEntity<HttpStatus> removeGrouping(Model model, String groupingId) throws IOException;

}
