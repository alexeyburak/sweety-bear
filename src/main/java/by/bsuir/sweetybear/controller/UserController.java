package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.dto.UserDTO;
import by.bsuir.sweetybear.exception.ApiRequestException;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Objects;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final ModelMapper modelMapper;

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") UserDTO user) {
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(@ModelAttribute("user") @Valid UserDTO user,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("passwordError", "Passwords do not match");
            return "registration";
        }

        User userDb = this.modelMapper.map(user, User.class);

        if (!userService.addUserToDatabase(userDb)) {
            model.addAttribute("errorMessage", "User with email " + user.getEmail() + " is already exists");
            return "registration";
        }
        return "redirect:/login";
    }

    @PostMapping("/login/error")
    public String loginError(@ModelAttribute("username") String email,
                             Model model) {
        User user = userService.getUserByEmail(email);

        if (!Objects.requireNonNull(user).isActive()) {
            model.addAttribute("messageError", "Your account was banned");
        }
        else {
            model.addAttribute("messageError", "Invalid username or password.");
        }
        return "login";
    }

    @GetMapping("/activate/{code}")
    public String activateUserAccount(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUserAccountAfterRegistration(code);

        if (isActivated) model.addAttribute("messageSuccess", "You successfully confirm your email");
        else model.addAttribute("messageSuccess", "Activation code is not available");

        return "login";
    }

    @GetMapping("/user/edit/{id}")
    public String edit(@PathVariable("id") Long id,
                       Model model,
                       Principal principal) {
        User userFromPrincipal = userService.getUserByPrincipal(principal);

        if (!Objects.equals(id, userFromPrincipal.getId()))
            return "redirect:/";

        model.addAttribute("userUpdate", userService.getUserById(id));
        model.addAttribute("user", userFromPrincipal);
        return "account-edit";
    }

    @PostMapping("/user/edit/{id}")
    public String update(@RequestParam("file1") MultipartFile file1,
                         @ModelAttribute("userUpdate") @Valid User user,
                         BindingResult bindingResult,
                         @PathVariable("id") Long id,
                         Model model,
                         Principal principal) throws IOException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userService.getUserByPrincipal(principal));
            return "account-edit";
        }

        userService.updateUserById(id, user, file1);
        return "redirect:/";
    }

    @PostMapping("/user/delete/{id}")
    public String deleteUserAccount(@PathVariable Long id) {
        userService.deleteUserAccountById(id);
        return "redirect:/login";
    }
}
