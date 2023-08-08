package com.imudataprocessor.api.controller.processtest;

import org.springframework.ui.Model;

import java.io.IOException;

public interface ProcessTestController {

    String generateMainTest(Model model);

    String generateSplitTest(Model model, String idTest) throws IOException;

    String processTest(Model model, String idTest, String testTypeName) throws IOException;


}
