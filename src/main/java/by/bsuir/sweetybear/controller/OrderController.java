package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.model.enums.OrderStatus;
import by.bsuir.sweetybear.service.OrderServiceImpl;
import by.bsuir.sweetybear.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Nov 2022
 */

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;
    private final UserServiceImpl userService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/orders/new")
    public String aboutOrderNew(Model model, Principal principal) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("orders", orderService.orderList(OrderStatus.NEW));
        return "orders";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/orders/approved")
    public String aboutOrderApproved(Model model, Principal principal) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("orders", orderService.orderList(OrderStatus.APPROVED));
        return "orders";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/orders/canceled")
    public String aboutOrderCanceled(Model model, Principal principal) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("orders", orderService.orderList(OrderStatus.CANCELED));
        return "orders";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/orders/closed")
    public String aboutOrderClosed(Model model, Principal principal) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("orders", orderService.orderList(OrderStatus.CLOSED));
        return "orders";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_USER')")
    @GetMapping("/orders/{id}")
    public String orderInfo(@PathVariable("id") Long id, Model model, Principal principal) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("order", orderService.getOrderById(id));
        return "order-info";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_USER')")
    @PostMapping("/orders/edit/{id}")
    public String orderUpdate(@PathVariable("id") Long id,
                              @RequestParam("status") OrderStatus status) {
        orderService.updateOrderStatusById(id, status);
        return "redirect:/orders/{id}";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_USER')")
    @GetMapping("/orders/user/{id}")
    public String userOrderInfo(@PathVariable("id") Long id, Model model, Principal principal) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("orders", orderService.getUserOrders(id));
        return "user-orders";
    }

}
