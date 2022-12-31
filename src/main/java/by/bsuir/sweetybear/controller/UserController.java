package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.dto.UserChangePasswordDTO;
import by.bsuir.sweetybear.dto.UserDTO;
import by.bsuir.sweetybear.model.User;
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
import java.io.IOException;
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

    private final UserServiceImpl userService;
    private final ModelMapper modelMapper;

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") UserDTO user) {
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") @Valid UserDTO user,
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

    @PostMapping("/login/error")
    public String loginError(@ModelAttribute("username") String email,
                             Model model) {
        model.addAttribute("messageError", ErrorMessage.USER_INVALID_DATA_INPUT);

        User user = userService.getUserByEmail(email);
        if (!Objects.requireNonNull(user).isActive()) {
            model.addAttribute("messageError", ErrorMessage.USER_ACCOUNT_BANNED);
        }

        return "login";
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
                                  Principal principal) {
        if (!Objects.equals(id, userService.getUserByPrincipal(principal).getId()))
            return "redirect:/";

        insertDataInModelForEditingUserAccount(model, principal, id);
        return "account-edit";
    }

    @PostMapping("/user/edit/{id}")
    public String updateUserAccountData(@RequestParam("file1") MultipartFile userAvatar,
                                        @ModelAttribute("userUpdateData") @Valid User user,
                                        BindingResult bindingResult,
                                        @PathVariable("id") Long id,
                                        Model model,
                                        Principal principal) throws IOException {
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

        userService.updateUserPasswordId(id, user);
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
