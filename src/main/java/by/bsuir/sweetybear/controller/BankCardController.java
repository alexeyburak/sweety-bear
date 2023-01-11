package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.dto.BankCardDTO;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.service.impl.BankCardServiceImpl;
import by.bsuir.sweetybear.service.impl.UserServiceImpl;
import by.bsuir.sweetybear.validator.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/payment")
public class BankCardController {

    private final UserServiceImpl userService;
    private final BankCardServiceImpl bankCardService;

    @GetMapping("/add")
    public String addBankCardForm(@ModelAttribute("bankCard") BankCardDTO bankCard,
                                  Principal principal,
                                  Model model) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "bank-card-add";
    }

    @PostMapping("/add")
    public String addBankCard(@ModelAttribute("bankCard") BankCardDTO bankCard,
                              Principal principal,
                              Model model) {
        User user = userService.getUserByPrincipal(principal);

        if (!bankCardService.addNewBankCard(bankCard, user)) {
            model.addAttribute("user", user);
            model.addAttribute("report", ErrorMessage.ADD_BANK_CARD_ERROR);
            return "bank-card-add";
        }

        return "redirect:/user/edit/" + user.getId() + "?tab=payments";
    }

    @PostMapping("/delete/{id}")
    public String deleteUserBankCard(@PathVariable Long id,
                                     HttpServletRequest request) {
        bankCardService.deleteBankCardById(id);
        return "redirect:" + request.getHeader("Referer");
    }
}
