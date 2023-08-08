package com.imudataprocessor.controller;

import com.imudataprocessor.api.configuration.pyrhonprogram.DataResultConfiguration;
import com.imudataprocessor.api.configuration.pyrhonprogram.ProgramConfiguration;
import com.imudataprocessor.api.controller.TestController;
import com.imudataprocessor.api.service.ExternalProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class TestControllerImpl implements TestController {

    @Autowired
    private ExternalProcess externalProcess;

    @Override
    @GetMapping("/generate-main-test")
    public String generateMainTest(final Model model) {
        return "test/main/main_test";
    }

    @Override
    @GetMapping("/generate-split-test")
    public String generateSplitTest(final Model model, final @RequestParam("idTest") String idTest) throws IOException {
        final List<String> nameTests = this.externalProcess.getAllNameTest();
        model.addAttribute("nameTests", nameTests);
        this.setValues(model, idTest);
        return "test/split/split_test";
    }

    @Override
    @GetMapping("/generate-process-test")
    public String processTest(final Model model, final @RequestParam("idTest") String idTest, final @RequestParam("testTypeName") String testTypeName) throws IOException {
        final Optional<ProgramConfiguration> programConfiguration = this.externalProcess.findByTestName(testTypeName);
        final List<String> groupDataList = programConfiguration
                .map(programConfiguration1 -> programConfiguration1.getDataResult().stream()
                        .map(DataResultConfiguration::getGroupData)
                        .filter(Objects::nonNull)
                        .distinct()
                        .toList())
                .orElse(Collections.emptyList());
        model.addAttribute("groupDataList", groupDataList);
        this.setValues(model, idTest);
        return "test/process/processed_test";
    }

    private void setValues(final Model model, final String id) {
        model.addAttribute("id", id);
        model.addAttribute("iframe", id + "Iframe");
        model.addAttribute("tableGraphic", id + "TableGraphic");
        model.addAttribute("spinner", id + "Spinner");
        model.addAttribute("accordionPanelsStayOpen", id + "AccordionPanelsStayOpen");
        model.addAttribute("panelsStayOpenCollapseOne", id + "PanelsStayOpen-collapseOne");
        model.addAttribute("idPanelsStayOpenCollapseOne", "#" + id + "PanelsStayOpen-collapseOne");
        model.addAttribute("panelsStayOpenCollapseTwo", id + "PanelsStayOpen-collapseTwo");
        model.addAttribute("idPanelsStayOpenCollapseTwo", "#" + id + "PanelsStayOpen-collapseTwo");
        model.addAttribute("panelsStayOpenCollapseThree", id + "PanelsStayOpen-collapseThree");
        model.addAttribute("idPanelsStayOpenCollapseThree", "#" + id + "PanelsStayOpen-collapseThree");
        model.addAttribute("quaternionGraphic", id + "QuaternionGraphic");
        model.addAttribute("gyroscopeGraphic", id + "GyroscopeGraphic");
        model.addAttribute("accelerometerGraphic", id + "AccelerometerGraphic");
        model.addAttribute("inputSplit", id + "InputSplit");
        model.addAttribute("processTestButton", id + "ProcessTestButton");
        model.addAttribute("downloadTestButton", id + "DownloadTestButton");
        model.addAttribute("deleteTestButton", id + "DeleteTestButton");
        model.addAttribute("alphanumericDataId", id + "AlphanumericDataId");
        model.addAttribute("errorProcessDataId", id + "ErrorProcessDataId");
    }

}








