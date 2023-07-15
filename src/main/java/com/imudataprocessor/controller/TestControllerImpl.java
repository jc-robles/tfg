package com.imudataprocessor.controller;

import com.imudataprocessor.api.controller.TestController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TestControllerImpl implements TestController {

    @GetMapping("/generate-main-test")
    public String generateMainTest(final Model model) {
        return "main_test";
    }

    @GetMapping("/generate-split-test")
    public String generateSplitTest(final Model model, final @RequestParam("idTest") String idTest) {
        this.setValues(model, idTest);
        return "split_test";
    }

    @GetMapping("/generate-process-test")
    public String processTest(final Model model, final @RequestParam("idTest") String idTest) {
        this.setValues(model, idTest);
        return "processed_test";
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
    }

}








