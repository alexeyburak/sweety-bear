package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.model.Product;
import by.bsuir.sweetybear.service.ProductService;
import by.bsuir.sweetybear.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
    private String admin(Model model) {
        model.addAttribute("users", userService.userList());
        return "admin";
    }

    @PostMapping("/admin/user/ban/{id}")
    public String userBan(@PathVariable("id") Long id) {
        userService.banUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/product/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "product-edit";
    }

    @PostMapping("/product/edit/{id}")
    public String update(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, Model model,
                         @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "product-edit";
        }
        productService.updateProductById(id, product);
        return "redirect:/product/{id}";
    }

}
