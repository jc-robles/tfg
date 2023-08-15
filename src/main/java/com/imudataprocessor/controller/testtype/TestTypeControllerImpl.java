package com.imudataprocessor.controller.testtype;

import com.imudataprocessor.api.configuration.GroupingItemConfiguration;
import com.imudataprocessor.api.configuration.TestGropingConfiguration;
import com.imudataprocessor.api.controller.testtype.TestTypeController;
import com.imudataprocessor.api.service.JsonService;
import com.imudataprocessor.api.service.TestGroupingService;
import com.imudataprocessor.api.service.TestTypeService;
import com.imudataprocessor.model.mapper.ProgramConfigurationMapper;
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
    private TestGroupingService testGroupingService;

    @Autowired
    private TestTypeService testTypeService;

    @Autowired
    private JsonService jsonService;

    @Autowired
    private ProgramConfigurationMapper programConfigurationMapper;

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
        final TestGropingConfiguration testGropingConfiguration = this.testGroupingService.getAllGrouping();

        final String dataNameFormatted = dataName.replace(" ", "_");
        model.addAttribute("dataName", dataName);
        model.addAttribute("dataNameFormatted", dataNameFormatted);
        model.addAttribute("dataNameId", dataNameFormatted + "Id");
        model.addAttribute("radioButtonId1", dataNameFormatted + "RadioButtonId1");
        model.addAttribute("radioButtonId2", dataNameFormatted + "RadioButtonId2");
        model.addAttribute("radioButtonName", dataNameFormatted + "RadioButtonName");
        model.addAttribute("selectDataNameId", dataNameFormatted + "SelectDataNameId");
        model.addAttribute("errorId", dataNameFormatted + "InvalidFeedbackEmptyGraphic");
        model.addAttribute("selectData", testGropingConfiguration.getSelect());
        return "test/create/output_data";
    }

    @Override
    @GetMapping("/test-type/create/graph")
    public String addGrouping(final Model model, final @RequestParam("groupingName") String groupingName) throws IOException {
        final GroupingItemConfiguration groupingItemConfiguration = this.testGroupingService.addGrouping(groupingName);
        model.addAttribute("allGraph", Collections.singletonList(groupingItemConfiguration));
        return "test/create/graph";
    }

    @Override
    @GetMapping("/test-type/all-graph")
    public String getAllGrouping(final Model model) throws IOException {
        final TestGropingConfiguration testGropingConfiguration = this.testGroupingService.getAllGrouping();
        model.addAttribute("allGraph", testGropingConfiguration.getSelect());
        return "test/create/graph";
    }

    @Override
    @GetMapping("/test-type/remove-grouping")
    public ResponseEntity<HttpStatus> removeGrouping(final Model model, final @RequestParam("groupingId") String groupingId) throws IOException {
        this.testGroupingService.removeGrouping(groupingId);
        return ResponseEntity.ok().build();
    }

    @Override
    @DeleteMapping("/test-type/delete")
    public ResponseEntity<HttpStatus> deleteTestType(final Model model, final @RequestParam("testType") String testType) throws IOException {
        this.testTypeService.removeTestType(testType);
        return ResponseEntity.ok().build();
    }

}
