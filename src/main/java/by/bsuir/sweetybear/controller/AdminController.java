package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.model.enums.Role;
import by.bsuir.sweetybear.service.PDFGeneratorServiceImpl;
import by.bsuir.sweetybear.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

import static by.bsuir.sweetybear.utils.Utils.getCurrentDateTime;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_OWNER')")
public class AdminController {

    private static final String PDF_GENERATION_FILENAME = "users_" + getCurrentDateTime() + ".pdf";
    private static final String PDF_CONTENT_TYPE  = "application/pdf";
    private static final String PDF_HEADER_KEY  = "Content-Disposition";

    private final UserServiceImpl userService;
    private final PDFGeneratorServiceImpl pdfGeneratorService;

    @GetMapping
    public String adminMenu(@RequestParam(name = "email", required = false) String email,
                            Model model,
                            Principal principal) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("users", userService.userList(email));
        model.addAttribute("email", email);
        return "admin";
    }

    @GetMapping("/users/pdf/export")
    public void generatePDF(HttpServletResponse response) {
        response.setContentType(PDF_CONTENT_TYPE);
        String headerValue = "attachment; filename=" + PDF_GENERATION_FILENAME;
        response.setHeader(PDF_HEADER_KEY, headerValue);

        pdfGeneratorService.exportUsersInPDF(response);
    }

    @PostMapping("/user/ban/{id}")
    public String banUserById(@PathVariable("id") Long id,
                              HttpServletRequest request) {
        userService.banUserAccountById(id);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/user/{id}")
    public String aboutUserById(@PathVariable("id") Long id,
                                Model model,
                                Principal principal) {
        model.addAttribute("userInfo", userService.getUserById(id));
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "user-info";
    }

    @GetMapping("/user/edit/{user}")
    public String editUserRole(@PathVariable("user") User user,
                               Model model,
                               Principal principal) {
        model.addAttribute("userToChange", user);
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("roles", Role.values());
        return "user-edit";
    }

    @PostMapping("/user/edit")
    public String editUserRole(@RequestParam("userId") User user) {
        userService.changeUserRole(user);
        return "redirect:/admin";
    }

}
