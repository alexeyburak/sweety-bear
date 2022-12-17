package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.dto.UserDTO;
import by.bsuir.sweetybear.dto.UserForgotPasswordDTO;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.service.impl.ForgotPasswordServiceImpl;
import by.bsuir.sweetybear.service.impl.UserServiceImpl;
import by.bsuir.sweetybear.validator.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

@Controller
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final ForgotPasswordServiceImpl forgotPasswordService;
    private final UserServiceImpl userService;
    private final ModelMapper modelmapper;

    @GetMapping("/forgot_password")
    public String forgotPassword(@ModelAttribute("email") String email) {
        return "forgot-password";
    }

    @PostMapping("/forgot_password")
    public String codeToResetPassword(Model model, @ModelAttribute("email") String email) {
        if (!forgotPasswordService.setCodeToResetUserPassword(email)) {
            model.addAttribute("reportError", "Your account was not found");
            return "forgot-password";
        }

        model.addAttribute("reportSuccess", "Check your email for further password recovery.");
        return "forgot-password";
    }

    @GetMapping("/reset_password/{code}")
    public String activateUserAccountByCode(@PathVariable String code,
                                            @ModelAttribute("user") UserForgotPasswordDTO user,
                                            Model model) {

        User userDB = userService.getUserByResetPasswordCode(code);

        if (userDB == null) {
            model.addAttribute("messageError", "Reset password code is not available");
            return "login";
        }
        model.addAttribute("code", code);

        return "reset-password";
    }
    
    @PostMapping("/reset_password")
    public String activateUserAccountByCode(Model model,
                                            @ModelAttribute("user") @Valid UserForgotPasswordDTO userDTO,
                                            BindingResult bindingResult,
                                            HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "reset-password";
        }

        User user = this.modelmapper.map(userDTO, User.class);

        String code = request.getParameter("code");
        if (forgotPasswordService.changeUserPassword(code, user))
            model.addAttribute("messageSuccess", "Reset");
        else
            model.addAttribute("messageError", "er");

        return "login";
    }

}
