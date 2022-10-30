package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.model.Product;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.model.enums.Role;
import by.bsuir.sweetybear.service.ProductService;
import by.bsuir.sweetybear.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    private final ProductService productService;

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("users", userService.userList());
        return "admin";
    }

    @PostMapping("/admin/user/ban/{id}")
    public String userBan(@PathVariable("id") Long id) {
        userService.banUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/user/{id}")
    public String userInfo(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "user-info";
    }

    @GetMapping("/admin/user/edit/{user}")
    public String userEdit(@PathVariable("user") User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "user-edit";
    }

    @PostMapping("/admin/user/edit")
    public String userUpdate(@RequestParam("userId") User user) {
        userService.changeUserRole(user);
        return "redirect:/admin";
    }

    @GetMapping("/product/edit/{id}")
    public String productEdit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "product-edit";
    }

    @PostMapping("/product/edit/{id}")
    public String productUpdate(@ModelAttribute("product") @Valid Product product,
                                BindingResult bindingResult,
                                Model model,
                         @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "product-edit";
        }
        productService.updateProductById(id, product);
        return "redirect:/product/{id}";
    }

}
