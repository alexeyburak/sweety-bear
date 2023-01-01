package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.service.impl.UserServiceImpl;
import by.bsuir.sweetybear.validator.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Controller
@RequiredArgsConstructor
public class ErrorController {

    private final UserServiceImpl userService;

    @GetMapping("/error")
    public String errorPage() {
        return "error";
    }

    @PostMapping("/login/error")
    public String loginError(@ModelAttribute("username") String email,
                             Model model) {
        model.addAttribute("messageError", ErrorMessage.USER_INVALID_DATA_INPUT);

        User user = userService.getUserByEmail(email);
        if (!Objects.requireNonNull(user).isActive()) {
            model.addAttribute("messageError", ErrorMessage.USER_ACCOUNT_BANNED);
        }
        return "login";
    }
}
