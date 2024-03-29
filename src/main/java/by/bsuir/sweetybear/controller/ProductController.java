package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.dto.feedback.FeedbackDTO;
import by.bsuir.sweetybear.dto.product.ProductDTO;
import by.bsuir.sweetybear.model.Product;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.model.enums.FeedbackSortType;
import by.bsuir.sweetybear.model.enums.ProductSortType;
import by.bsuir.sweetybear.service.impl.FeedbackServiceImpl;
import by.bsuir.sweetybear.service.impl.ProductRatingServiceImpl;
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
    private final FeedbackServiceImpl feedbackService;
    private final UserServiceImpl userService;
    private final ProductRatingServiceImpl productRatingService;
    private final ModelMapper modelMapper;

    @GetMapping("/")
    public String products(@RequestParam(name = "title", required = false) String title,
                           @RequestParam(name = "sort", required = false) ProductSortType type,
                           Principal principal,
                           Model model) {
        User userPrincipal = userService.getUserByPrincipal(principal);

        if (!userPrincipal.isActive() && userPrincipal.getEmail() != null)
            return "redirect:/login";

        model.addAttribute("products", productService.listProducts(title, type));
        model.addAttribute("user", userPrincipal);
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("user_favorites", userPrincipal.getFavoriteProductsIds());
        model.addAttribute("title", title);
        return "products";
    }

    @GetMapping("/product/{id}")
    public String aboutProduct(@PathVariable Long id,
                               Model model,
                               Principal principal,
                               @ModelAttribute("feedback") @Valid FeedbackDTO feedbackDTO,
                               @RequestParam(name = "sort", required = false) FeedbackSortType type) {
        Product product = productService.getProductById(id);
        User userPrincipal = userService.getUserByPrincipal(principal);

        model.addAttribute("product", product);
        model.addAttribute("user_favorites", userPrincipal.getFavoriteProductsIds());
        model.addAttribute("images", product.getImages());
        model.addAttribute("user", userPrincipal);
        model.addAttribute("rating", productRatingService.generateProductRating(id));
        model.addAttribute("feedbacks", feedbackService.getProductFeedbackListWithSortingType(id, type));
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

        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/product/favorite/{productId}")
    public String addProductToUserFavorites(@PathVariable Long productId,
                                     Principal principal,
                                     HttpServletRequest request) {
        Product favoriteProduct = productService.getProductById(productId);
        userService.addProductToFavoritesAndRemoveIfExists(principal.getName(), favoriteProduct);

        return "redirect:" + request.getHeader("Referer");
    }

}
