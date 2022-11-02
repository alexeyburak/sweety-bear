package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.model.enums.Role;
import by.bsuir.sweetybear.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private final UserService userService;

    @GetMapping("/admin")
    public String admin(Model model, Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        model.addAttribute("users", userService.userList(user));
        return "admin";
    }

    @PostMapping("/admin/user/ban/{id}")
    public String userBan(@PathVariable("id") Long id) {
        userService.banUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/user/{id}")
    public String userInfo(@PathVariable("id") Long id, Model model, Principal principal) {
        model.addAttribute("userInfo", userService.getUserById(id));
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "user-info";
    }

    @GetMapping("/admin/user/edit/{user}")
    public String userEdit(@PathVariable("user") User user, Model model, Principal principal) {
        model.addAttribute("userToChange", user);
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("roles", Role.values());
        return "user-edit";
    }

    @PostMapping("/admin/user/edit")
    public String userUpdate(@RequestParam("userId") User user) {
        userService.changeUserRole(user);
        return "redirect:/admin";
    }

}
