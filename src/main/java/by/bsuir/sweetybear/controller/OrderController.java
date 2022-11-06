package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.model.enums.OrderStatus;
import by.bsuir.sweetybear.service.OrderServiceImpl;
import by.bsuir.sweetybear.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Nov 2022
 */

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class OrderController {

    private final OrderServiceImpl orderService;
    private final UserServiceImpl userService;

    @GetMapping("/orders/new")
    public String aboutOrderNew(Model model, Principal principal) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("orders", orderService.orderList(OrderStatus.NEW));
        return "orders";
    }

    @GetMapping("/orders/approved")
    public String aboutOrderApproved(Model model, Principal principal) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("orders", orderService.orderList(OrderStatus.APPROVED));
        return "orders";
    }

    @GetMapping("/orders/canceled")
    public String aboutOrderCanceled(Model model, Principal principal) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("orders", orderService.orderList(OrderStatus.CANCELED));
        return "orders";
    }

    @GetMapping("/orders/closed")
    public String aboutOrderClosed(Model model, Principal principal) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("orders", orderService.orderList(OrderStatus.CLOSED));
        return "orders";
    }

    @GetMapping("/orders/{id}")
    public String orderInfo(@PathVariable("id") Long id, Model model, Principal principal) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("order", orderService.getOrderById(id));
        return "order-info";
    }

    @PostMapping("/orders/edit/{id}")
    public String orderUpdate(@PathVariable("id") Long id,
                              @RequestParam("status") OrderStatus status) {
        orderService.updateOrderStatusById(id, status);
        return "redirect:/orders/{id}";
    }



}
