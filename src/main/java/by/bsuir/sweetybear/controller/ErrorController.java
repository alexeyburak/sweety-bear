package by.bsuir.sweetybear.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Controller
public class ErrorController {

    @GetMapping("/error")
    public String errorPage(Model model) {
        return "error";
    }
}
