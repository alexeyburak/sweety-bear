package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.dto.user.UserChangePasswordDTO;
import by.bsuir.sweetybear.dto.user.UserRegistrationDTO;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.service.impl.BankCardServiceImpl;
import by.bsuir.sweetybear.service.impl.FeedbackServiceImpl;
import by.bsuir.sweetybear.service.impl.UserServiceImpl;
import by.bsuir.sweetybear.validator.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Objects;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Controller
@RequiredArgsConstructor
public class UserController {

    private static final String ACCOUNT_FEEDBACKS = "feedbacks";
    private static final String ACCOUNT_BANK_CARDS = "payments";
    private final UserServiceImpl userService;
    private final FeedbackServiceImpl feedbackService;
    private final BankCardServiceImpl bankCardService;
    private final ModelMapper modelMapper;

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") UserRegistrationDTO user) {
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") @Valid UserRegistrationDTO user,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        User userDb = this.modelMapper.map(user, User.class);

        if (!userService.addUserToDatabase(userDb)) {
            model.addAttribute("errorMessage", "User with email " + user.getEmail() + " is already exists");
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/user/favorites")
    public String favoriteProducts(Model model,
                                   Principal principal) {
        User user = userService.getUserByPrincipal(principal);

        model.addAttribute("user", user);
        model.addAttribute("products", user.getFavoriteProducts());
        return "user-favorites";
    }

    @GetMapping("/activate/{code}")
    public String activateUserAccountByCode(Model model,
                                            @PathVariable String code) {
        boolean isActivated = userService.activateUserAccountAfterRegistration(code);

        if (isActivated)
            model.addAttribute("messageSuccess", ErrorMessage.USER_CONFIRM_EMAIL);
        else
            model.addAttribute("messageError", ErrorMessage.USER_INVALID_ACTIVATION_CODE);

        return "login";
    }

    @GetMapping("/user/edit/{id}")
    public String editUserAccount(@PathVariable("id") Long id,
                                  Model model,
                                  Principal principal,
                                  @RequestParam(name = "tab", required = false) String tab) {
        if (!Objects.equals(id, userService.getUserByPrincipal(principal).getId()))
            return "redirect:/";
        if (tab != null)
            chooseAccountTabsThenAddToModel(tab, model, id);

        insertDataInModelForEditingUserAccount(model, principal, id);
        return "account-edit";
    }

    private void chooseAccountTabsThenAddToModel(String tab, Model model, Long id) {
        switch (tab) {
            case ACCOUNT_FEEDBACKS -> model.addAttribute(ACCOUNT_FEEDBACKS, feedbackService.getUserFeedbackList(id));
            case ACCOUNT_BANK_CARDS -> model.addAttribute(ACCOUNT_BANK_CARDS, bankCardService.getBankCardDTOListByUserId(id));
        }
    }

    @PostMapping("/user/edit/{id}")
    public String updateUserAccountData(@RequestParam("file1") MultipartFile userAvatar,
                                        @ModelAttribute("userUpdateData") @Valid User user,
                                        BindingResult bindingResult,
                                        @PathVariable("id") Long id,
                                        Model model,
                                        Principal principal) {
        User principalUser = userService.getUserByPrincipal(principal);
        insertDataInModelForEditingUserAccount(model, principal, id);

        if (bindingResult.hasErrors()) {
            return "account-edit";
        }

        User userDb = userService.getUserByEmail(user.getEmail());
        if (userDb != null && userDb != principalUser) {
            model.addAttribute("messageError", ErrorMessage.USER_EMAIL_EXISTS);
            return "account-edit";
        }

        userService.updateUserDataById(id, user, userAvatar);
        return "redirect:/user/edit/{id}";
    }

    @PostMapping("/user/edit_password/{id}")
    public String updateUserAccountPassword(Model model,
                                            @PathVariable("id") Long id,
                                            @ModelAttribute("userUpdatePassword") @Valid UserChangePasswordDTO userDTO,
                                            BindingResult bindingResult,
                                            Principal principal) {
        insertDataInModelForEditingUserAccount(model, principal, id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("messageChangingPassword", ErrorMessage.RESET_PASSWORD_ERROR);
            return "account-edit";
        }

        User user = this.modelMapper.map(userDTO, User.class);

        userService.updateUserPasswordById(id, user);
        return "redirect:/user/edit/{id}";
    }

    private void insertDataInModelForEditingUserAccount(Model model, Principal principal, Long id) {
        model.addAttribute("userUpdateData", userService.getUserById(id));
        model.addAttribute("userUpdatePassword", new UserChangePasswordDTO());
        model.addAttribute("user", userService.getUserByPrincipal(principal));
    }

    @PostMapping("/user/delete/{id}")
    public String deleteUserAccount(@PathVariable Long id) {
        userService.deleteUserAccountById(id);
        return "redirect:/login";
    }

    @PostMapping("/user/delete/image/{id}")
    public String deleteUserAvatar(@PathVariable Long id) {
        userService.deleteUserAvatarById(id);
        return "redirect:/user/edit/{id}";
    }
}
