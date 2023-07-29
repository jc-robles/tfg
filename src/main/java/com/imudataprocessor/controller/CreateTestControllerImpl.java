package com.imudataprocessor.controller;

import com.imudataprocessor.api.configuration.GroupingItemConfiguration;
import com.imudataprocessor.api.configuration.TestGropingConfiguration;
import com.imudataprocessor.api.controller.CreateTestController;
import com.imudataprocessor.api.service.TestGroupingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Collections;

@Controller
public class CreateTestControllerImpl implements CreateTestController {

    @Autowired
    private TestGroupingService testGroupingService;

    @Override
    @GetMapping("/create-test/add-output-data")
    public String addOutputData(final Model model, final @RequestParam("dataName") String dataName) throws IOException {
        final TestGropingConfiguration testGropingConfiguration = testGroupingService.getAllGrouping();

        final String dataNameFormatted = dataName.replace(" ", "_");
        model.addAttribute("dataName", dataName);
        model.addAttribute("dataNameId", dataNameFormatted + "Id");
        model.addAttribute("radioButtonId1", dataNameFormatted + "RadioButtonId1");
        model.addAttribute("radioButtonId2", dataNameFormatted + "RadioButtonId2");
        model.addAttribute("radioButtonName", dataNameFormatted + "RadioButtonName");
        model.addAttribute("selectDataNameId", dataNameFormatted + "SelectDataNameId");
        model.addAttribute("selectData", testGropingConfiguration.getSelect());
        return "create_test/add_output_data";
    }

    @Override
    @GetMapping("/create-test/add-grouping")
    public String addGrouping(final Model model, final @RequestParam("groupingName") String groupingName) throws IOException {
        GroupingItemConfiguration groupingItemConfiguration = testGroupingService.addGrouping(groupingName);
        model.addAttribute("allGrouping", Collections.singletonList(groupingItemConfiguration));
        return "create_test/add_grouping";
    }

    @Override
    @GetMapping("/create-test/all-grouping")
    public String getAllGrouping(Model model) throws IOException {
        final TestGropingConfiguration testGropingConfiguration = testGroupingService.getAllGrouping();
        model.addAttribute("allGrouping", testGropingConfiguration.getSelect());
        return "create_test/add_grouping";
    }

    @Override
    @GetMapping("/create-test/remove-grouping")
    public ResponseEntity<HttpStatus> removeGrouping(Model model, final @RequestParam("groupingId") String groupingId) throws IOException {
        testGroupingService.removeGrouping(groupingId);
        return ResponseEntity.ok().build();
    }
}
