package com.imudataprocessor.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.io.IOException;

public interface CreateTestController {

    String addOutputData(Model model, String dataName) throws IOException;

    String addGrouping(Model model, String groupingName) throws IOException;

    String getAllGrouping(Model model) throws IOException;

    ResponseEntity<HttpStatus> removeGrouping(Model model, String groupingId) throws IOException;

}
