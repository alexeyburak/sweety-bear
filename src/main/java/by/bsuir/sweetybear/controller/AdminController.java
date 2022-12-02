package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.model.enums.Role;
import by.bsuir.sweetybear.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_OWNER')")
public class AdminController {

    private final UserServiceImpl userService;

    @GetMapping("/admin")
    public String adminMenu(@RequestParam(name = "email", required = false) String email,
                            Model model,
                            Principal principal) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("users", userService.userList(email));
        model.addAttribute("email", email);
        return "admin";
    }

    @PostMapping("/admin/user/ban/{id}")
    public String banUserById(@PathVariable("id") Long id,
                              HttpServletRequest request) {
        userService.banUserAccountById(id);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/user/{id}")
    public String aboutUserById(@PathVariable("id") Long id,
                                Model model,
                                Principal principal) {
        model.addAttribute("userInfo", userService.getUserById(id));
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "user-info";
    }

    @GetMapping("/admin/user/edit/{user}")
    public String editUserRole(@PathVariable("user") User user,
                               Model model,
                               Principal principal) {
        model.addAttribute("userToChange", user);
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("roles", Role.values());
        return "user-edit";
    }

    @PostMapping("/admin/user/edit")
    public String editUserRole(@RequestParam("userId") User user) {
        userService.changeUserRole(user);
        return "redirect:/admin";
    }

}
