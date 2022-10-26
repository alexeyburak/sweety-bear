package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

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
