package com.imudataprocessor.api.controller.testtype;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TestTypeController {

    ResponseEntity<HttpStatus> createTest(Model model, String createTest, MultipartFile fileTest) throws IOException;

    ResponseEntity<List<String>> getAllTestType(final Model model) throws IOException;

    String addOutputData(Model model, String dataName) throws IOException;

    String addGraph(Model model, String graphName) throws IOException;

    String getAllGraphs(Model model) throws IOException;

    ResponseEntity<HttpStatus> removeGraph(Model model, String graphId) throws IOException;

    ResponseEntity<HttpStatus> deleteTestType(Model model, String testType) throws IOException;

}
