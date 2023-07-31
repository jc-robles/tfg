package com.imudataprocessor.api.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

public interface TestController {

    String generateMainTest(final Model model);

    String generateSplitTest(final Model model, final @RequestParam("idTest") String idTest) throws IOException;

    String processTest(final Model model, final @RequestParam("idTest") String idTest);


}
