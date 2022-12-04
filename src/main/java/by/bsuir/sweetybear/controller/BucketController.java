package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.dto.BucketDTO;
import by.bsuir.sweetybear.model.Address;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.service.BucketServiceImpl;
import by.bsuir.sweetybear.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Nov 2022
 */

@Controller
@RequiredArgsConstructor
public class BucketController {

    private final BucketServiceImpl bucketService;
    private final UserServiceImpl userService;

    @GetMapping("/bucket")
    public String aboutBucket(@ModelAttribute("address") Address address,
                              Model model,
                              Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        if (principal == null) {
            model.addAttribute("bucket", new BucketDTO());
        } else {
            BucketDTO bucketDTO = bucketService.getBucketByUser(user.getEmail());
            model.addAttribute("bucket", bucketDTO);
        }
        return "bucket";
    }

    @PostMapping("/bucket/delete/{id}")
    public String deleteProductByIdFromBucket(@PathVariable Long id,
                                              Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        bucketService.deleteProductFromBucket(user.getBucket(), id);
        return "redirect:/bucket";
    }

    @PostMapping("/bucket")
    public String commitBucketToOrder(@ModelAttribute("address") Address address,
                                      Principal principal) {
        User userFromDB = userService.getUserByPrincipal(principal);

        if (userFromDB != null)
            bucketService.addBucketToOrder(userFromDB.getEmail(), address);

        return "redirect:/bucket";
    }
}
