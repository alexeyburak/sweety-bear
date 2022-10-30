package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") User user) {
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (!userService.createUser(user)) {
            model.addAttribute("errorMessage", "Пользователь с Email: " + user.getEmail() + " уже существует");
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/user/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "account-edit";
    }

    @PostMapping("/user/edit/{id}")
    public String update(@RequestParam("file1") MultipartFile file1,
                         @ModelAttribute("user") User user,
                         @PathVariable("id") Long id) throws IOException {
        userService.updateUserById(id, user, file1);
        return "redirect:/";
    }

}
