package com.imudataprocessor.controller.testtype;

import com.imudataprocessor.api.configuration.GraphItemConfiguration;
import com.imudataprocessor.api.configuration.TestGraphConfiguration;
import com.imudataprocessor.api.controller.testtype.TestTypeController;
import com.imudataprocessor.api.service.JsonService;
import com.imudataprocessor.api.service.TestGraphService;
import com.imudataprocessor.api.service.TestTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Controller
public class TestTypeControllerImpl implements TestTypeController {

    @Autowired
    private TestGraphService testGraphService;

    @Autowired
    private TestTypeService testTypeService;

    @Autowired
    private JsonService jsonService;

    @Override
    @PostMapping("/test-type/create")
    public ResponseEntity<HttpStatus> createTest(final Model model, final @RequestParam("createTest") String createTest,
                                                 @RequestParam("fileTest") final MultipartFile fileTest) throws IOException {
        this.testTypeService.createTestType(createTest, fileTest);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/test-type/all-test-type")
    public ResponseEntity<List<String>> getAllTestType(final Model model) throws IOException {
        return ResponseEntity.ok().body(this.testTypeService.getAllNameTest());
    }

    @Override
    @GetMapping("/test-type/add-output-data")
    public String addOutputData(final Model model, final @RequestParam("dataName") String dataName) throws IOException {
        final TestGraphConfiguration testGraphConfiguration = this.testGraphService.getAllGraphs();

        final String dataNameFormatted = dataName.replace(" ", "_");
        model.addAttribute("dataName", dataName);
        model.addAttribute("dataNameFormatted", dataNameFormatted);
        model.addAttribute("dataNameId", dataNameFormatted + "Id");
        model.addAttribute("radioButtonId1", dataNameFormatted + "RadioButtonId1");
        model.addAttribute("radioButtonId2", dataNameFormatted + "RadioButtonId2");
        model.addAttribute("radioButtonName", dataNameFormatted + "RadioButtonName");
        model.addAttribute("selectDataNameId", dataNameFormatted + "SelectDataNameId");
        model.addAttribute("errorId", dataNameFormatted + "InvalidFeedbackEmptyGraphic");
        model.addAttribute("selectData", testGraphConfiguration.getSelect());
        return "test/create/output_data";
    }

    @Override
    @GetMapping("/test-type/create/graph")
    public String addGraph(final Model model, final @RequestParam("graphName") String graphName) throws IOException {
        final GraphItemConfiguration graphItemConfiguration = this.testGraphService.addGraph(graphName);
        model.addAttribute("allGraph", Collections.singletonList(graphItemConfiguration));
        return "test/create/graph";
    }

    @Override
    @GetMapping("/test-type/all-graph")
    public String getAllGraphs(final Model model) throws IOException {
        final TestGraphConfiguration testGraphConfiguration = this.testGraphService.getAllGraphs();
        model.addAttribute("allGraph", testGraphConfiguration.getSelect());
        return "test/create/graph";
    }

    @Override
    @GetMapping("/test-type/remove-graph")
    public ResponseEntity<HttpStatus> removeGraph(final Model model, final @RequestParam("graphId") String graphId) throws IOException {
        this.testGraphService.removeGraph(graphId);
        return ResponseEntity.ok().build();
    }

    @Override
    @DeleteMapping("/test-type/delete")
    public ResponseEntity<HttpStatus> deleteTestType(final Model model, final @RequestParam("testType") String testType) throws IOException {
        this.testTypeService.removeTestType(testType);
        return ResponseEntity.ok().build();
    }

}
