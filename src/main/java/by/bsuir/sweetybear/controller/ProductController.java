package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.dto.ProductDTO;
import by.bsuir.sweetybear.model.Product;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.model.enums.SortType;
import by.bsuir.sweetybear.service.ProductServiceImpl;
import by.bsuir.sweetybear.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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

    private final ProductServiceImpl productService;
    private final UserServiceImpl userService;
    private final ModelMapper modelMapper;

    @GetMapping("/")
    public String products(@RequestParam(name = "title", required = false) String title,
                           @RequestParam(name = "sort", required = false) SortType type,
                           Principal principal,
                           Model model) {
        model.addAttribute("products", productService.listProducts(title, type));
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("title", title);
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

    private void fillUserToModelAfterBindingResultError(Model model, Principal principal) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("error", "error");
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_OWNER')")
    @PostMapping("/product/create")
    public String createProduct(@RequestParam("file1") MultipartFile file1,
                                @RequestParam("file2") MultipartFile file2,
                                @ModelAttribute("product") @Valid ProductDTO product,
                                BindingResult bindingResult,
                                Model model,
                                Principal principal) throws IOException {
        if (bindingResult.hasErrors()) {
            fillUserToModelAfterBindingResultError(model, principal);
            return "products";
        }

        Product productDb = this.modelMapper.map(product, Product.class);

        productService.addProductToDatabase(productDb, file1, file2);

        return "redirect:/";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_OWNER')")
    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_OWNER')")
    @GetMapping("/product/edit/{id}")
    public String productEdit(@PathVariable("id") Long id, Model model, Principal principal) {
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "product-edit";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_OWNER')")
    @PostMapping("/product/edit/{id}")
    public String productUpdate(@RequestParam("file1") MultipartFile file1,
                                @RequestParam("file2") MultipartFile file2,
                                @ModelAttribute("product") @Valid Product product,
                                BindingResult bindingResult,
                                @PathVariable("id") Long id,
                                Model model,
                                Principal principal) throws IOException {
        if (bindingResult.hasErrors()) {
            fillUserToModelAfterBindingResultError(model, principal);
            return "product-edit";
        }

        productService.updateProductById(id, product, file1, file2);
        return "redirect:/product/{id}";
    }

    @GetMapping("/{id}/bucket")
    public String addBucket(@PathVariable Long id,
                            Principal principal,
                            HttpServletRequest request) {
        User user = userService.getUserByPrincipal(principal);
        if (principal == null) {
            return "redirect:/";
        }
        productService.addProductIdToUserBucket(id, user.getEmail());

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

}
