package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.model.Product;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.service.ProductService;
import by.bsuir.sweetybear.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    @GetMapping("/")
    public String products(@RequestParam(name = "title", required = false) String title, Principal principal, Model model) {
        model.addAttribute("products", productService.listProducts(title));
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("product", new Product());
        return "products";
    }


    @GetMapping("/product/{id}")
    public String productInfo(@PathVariable Long id, Model model, Principal principal) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "product-info";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/product/create")
    public String createProduct(@RequestParam("file1") MultipartFile file1,
                                @RequestParam("file2") MultipartFile file2,
                                @ModelAttribute("product") @Valid Product product,
                                BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return "products";
        }
        productService.saveProduct(product, file1, file2);
        return "redirect:/";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/product/edit/{id}")
    public String productEdit(@PathVariable("id") Long id, Model model, Principal principal) {
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "product-edit";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/product/edit/{id}")
    public String productUpdate(@RequestParam("file1") MultipartFile file1,
                                @RequestParam("file2") MultipartFile file2,
                                @ModelAttribute("product") @Valid Product product,
                                BindingResult bindingResult,
                                @PathVariable("id") Long id) throws IOException {
        if (bindingResult.hasErrors()) {
            return "product-edit";
        }
        productService.updateProductById(id, product, file1, file2);
        return "redirect:/product/{id}";
    }

    @GetMapping("/{id}/bucket")
    public String addBucket(@PathVariable Long id, Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        if (principal == null) {
            return "redirect:/";
        }
        productService.addToUserBucket(id, user.getEmail());
        return "redirect:/";
    }

}
