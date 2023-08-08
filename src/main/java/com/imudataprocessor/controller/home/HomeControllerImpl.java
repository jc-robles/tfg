package com.imudataprocessor.controller.home;

import com.imudataprocessor.api.controller.home.HomeController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeControllerImpl implements HomeController {

    @Override
    @GetMapping("/")
    public String home(final Model theModel) {
        return "home/home";
    }

}








