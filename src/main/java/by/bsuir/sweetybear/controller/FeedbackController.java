package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.dto.FeedbackDTO;
import by.bsuir.sweetybear.model.Feedback;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.service.impl.FeedbackServiceImpl;
import by.bsuir.sweetybear.service.impl.ProductServiceImpl;
import by.bsuir.sweetybear.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackServiceImpl feedbackService;
    private final UserServiceImpl userService;
    private final ProductServiceImpl productService;
    private final ModelMapper modelMapper;

    @PostMapping("/add/{productId}")
    public String addFeedback(@PathVariable("productId") Long id,
                              Principal principal,
                              Model model,
                              @ModelAttribute("feedback") @Valid FeedbackDTO feedbackDTO,
                              BindingResult bindingResult) {
        User user = userService.getUserByPrincipal(principal);
        if (bindingResult.hasErrors()) {
            model.addAttribute("product", productService.getProductById(id));
            model.addAttribute("error", "error");
            model.addAttribute("user", user);
            return "product-info";
        }

        Feedback feedback = this.modelMapper.map(feedbackDTO, Feedback.class);
        feedbackService.addFeedback(user, id, feedback);
        return "redirect:/product/{productId}";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_OWNER')")
    @PostMapping("/delete/{id}")
    public String deleteFeedback(@PathVariable("id") Long id,
                                 HttpServletRequest request) {
        feedbackService.deleteFeedback(id);
        return "redirect:" + request.getHeader("Referer");
    }

}
