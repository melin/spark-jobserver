package io.github.melin.spark.jobserver.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @Value("${spring.profiles.active}")
    protected String profiles;

    @RequestMapping("/")
    public String home(ModelMap model) {
        model.addAttribute("profiles", profiles);
        return "index";
    }
}
