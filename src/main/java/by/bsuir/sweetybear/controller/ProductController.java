package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.dto.ProductDTO;
import by.bsuir.sweetybear.model.Product;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.model.enums.SortType;
import by.bsuir.sweetybear.service.impl.ProductServiceImpl;
import by.bsuir.sweetybear.service.impl.UserServiceImpl;
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
        User userPrincipal = userService.getUserByPrincipal(principal);

        if (!userPrincipal.isActive() && userPrincipal.getEmail() != null)
            return "redirect:/login";

        model.addAttribute("products", productService.listProducts(title, type));
        model.addAttribute("user", userPrincipal);
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("title", title);
        return "products";
    }

    @GetMapping("/product/{id}")
    public String aboutProduct(@PathVariable Long id, Model model, Principal principal) {
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
    public String createProduct(@RequestParam("file1") MultipartFile previewProductImage,
                                @RequestParam("file2") MultipartFile productImage,
                                @ModelAttribute("product") @Valid ProductDTO product,
                                BindingResult bindingResult,
                                Model model,
                                Principal principal) throws IOException {
        if (bindingResult.hasErrors()) {
            fillUserToModelAfterBindingResultError(model, principal);
            return "products";
        }

        Product productDb = this.modelMapper.map(product, Product.class);

        productService.addProductToDatabase(productDb, previewProductImage, productImage);

        return "redirect:/";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_OWNER')")
    @PostMapping("/product/delete/{id}")
    public String deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_OWNER')")
    @GetMapping("/product/edit/{id}")
    public String editProductById(@PathVariable("id") Long id, Model model, Principal principal) {
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "product-edit";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_OWNER')")
    @PostMapping("/product/edit/{id}")
    public String updateProductById(@RequestParam("file1") MultipartFile previewProductImage,
                                @RequestParam("file2") MultipartFile productImage,
                                @ModelAttribute("product") @Valid Product product,
                                BindingResult bindingResult,
                                @PathVariable("id") Long id,
                                Model model,
                                Principal principal) throws IOException {
        if (bindingResult.hasErrors()) {
            fillUserToModelAfterBindingResultError(model, principal);
            return "product-edit";
        }

        productService.updateProductById(id, product, previewProductImage, productImage);
        return "redirect:/product/{id}";
    }

    @GetMapping("/{id}/bucket")
    public String addProductToBucket(@PathVariable Long id,
                            Principal principal,
                            HttpServletRequest request) {
        User user = userService.getUserByPrincipal(principal);

        productService.addProductIdToUserBucket(id, user.getEmail());

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/product/favorite/{productId}")
    public String addProductToUserFavorites(@PathVariable Long productId,
                                     Principal principal,
                                     HttpServletRequest request) {
        Product favoriteProduct = productService.getProductById(productId);
        userService.addProductToFavorites(principal.getName(), favoriteProduct);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }


}
