package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.service.impl.ForgotPasswordServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

@Controller
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final ForgotPasswordServiceImpl forgotPasswordService;

    @GetMapping("/forgot_password")
    public String codeToResetPassword(@ModelAttribute("email") String email) {
        return "";
    }

    @PostMapping("/forgot_password")
    public String codeToResetPassword(Model model, @ModelAttribute("email") String email) {

        if (!forgotPasswordService.setCodeToResetUserPassword(email))
            return "";

        return "";
    }

}
