package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.dto.UserChangePasswordDTO;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
            model.addAttribute("reportError", ErrorMessage.RESET_PASSWORD_NOT_FOUND_ACCOUNT);
            return "forgot-password";
        }

        model.addAttribute("reportSuccess", ErrorMessage.RESET_PASSWORD_CHECK_EMAIL);
        return "forgot-password";
    }

    @GetMapping("/reset_password/{code}")
    public String activateUserAccountByCode(@PathVariable String code,
                                            @ModelAttribute("user") UserChangePasswordDTO user,
                                            Model model) {

        User userDB = userService.getUserByResetPasswordCode(code);

        if (userDB == null) {
            model.addAttribute("messageError", ErrorMessage.USER_INVALID_ACTIVATION_CODE);
            return "login";
        }

        model.addAttribute("code", code);
        return "reset-password";
    }
    
    @PostMapping("/reset_password")
    public String activateUserAccountByCode(Model model,
                                            @ModelAttribute("user") @Valid UserChangePasswordDTO userDTO,
                                            BindingResult bindingResult,
                                            HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "reset-password";
        }

        User user = this.modelmapper.map(userDTO, User.class);

        String code = request.getParameter("code");
        if (forgotPasswordService.changeUserPasswordByCode(code, user))
            model.addAttribute("messageSuccess", ErrorMessage.RESET_PASSWORD_SUCCESS);
        else
            model.addAttribute("messageError", ErrorMessage.RESET_PASSWORD_ERROR);

        return "login";
    }

}
