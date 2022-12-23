package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.dto.AddressDTO;
import by.bsuir.sweetybear.dto.BucketDTO;
import by.bsuir.sweetybear.model.Address;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.service.impl.BucketServiceImpl;
import by.bsuir.sweetybear.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
 * Oct 2022
 */

@Controller
@RequiredArgsConstructor
public class BucketController {

    private final BucketServiceImpl bucketService;
    private final UserServiceImpl userService;
    private final ModelMapper modelMapper;

    @GetMapping("/bucket")
    public String aboutBucket(@ModelAttribute("address") AddressDTO address,
                              Model model,
                              Principal principal) {
        User userPrincipal = userService.getUserByPrincipal(principal);
        if (!userPrincipal.isActive())
            return "redirect:/login";

        model.addAttribute("user", userPrincipal);

        BucketDTO bucketDTO = bucketService.getBucketByUser(userPrincipal.getEmail());
        model.addAttribute("bucket", bucketDTO);
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
    public String commitBucketToOrder(@ModelAttribute("address") AddressDTO address,
                                      Principal principal) {
        User userFromDB = userService.getUserByPrincipal(principal);

        Address addressDb = this.modelMapper.map(address, Address.class);

        if (userFromDB != null)
            bucketService.addBucketToOrder(userFromDB.getEmail(), addressDb);

        return "redirect:/bucket?success";
    }
}
