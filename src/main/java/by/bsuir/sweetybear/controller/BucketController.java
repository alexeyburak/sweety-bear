package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.dto.AddressDTO;
import by.bsuir.sweetybear.model.Address;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.service.impl.BucketServiceImpl;
import by.bsuir.sweetybear.service.impl.UserServiceImpl;
import by.bsuir.sweetybear.validator.ErrorMessage;
import by.bsuir.sweetybear.validator.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

        addDataToBucketModel(model, userPrincipal);
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
                                      @RequestParam("isDelivery") boolean isDelivery,
                                      Model model,
                                      Principal principal) {
        User userPrincipal = userService.getUserByPrincipal(principal);
        boolean isAddressValid = ValidatorFactory.getInstance().getAddressValidator().isValid(address);

        if (!isAddressValid && isDelivery) {
            model.addAttribute("errorMessage", ErrorMessage.ADDRESS_INVALID_DATA_INPUT);
            addDataToBucketModel(model, userPrincipal);
            return "bucket";
        }

        Address addressDb = this.modelMapper.map(address, Address.class);

        if (userPrincipal != null)
            bucketService.addBucketToOrder(userPrincipal.getEmail(), addressDb, isDelivery);
        return "redirect:/bucket?success";
    }

    private void addDataToBucketModel(Model model, User userPrincipal) {
        model.addAttribute("user", userPrincipal);
        model.addAttribute("bucket", bucketService.getBucketByUser(userPrincipal.getEmail()));
    }
}
