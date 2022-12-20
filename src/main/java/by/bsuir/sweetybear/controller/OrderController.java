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

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;
    private final UserServiceImpl userService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_OWNER')")
    @GetMapping("/orders/new")
    public String aboutOrderNew(Model model, Principal principal) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("orders", orderService.orderListFindByStatus(OrderStatus.NEW));
        return "orders";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_OWNER')")
    @GetMapping("/orders/approved")
    public String aboutOrderApproved(Model model, Principal principal) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("orders", orderService.orderListFindByStatus(OrderStatus.APPROVED));
        return "orders";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_OWNER')")
    @GetMapping("/orders/canceled")
    public String aboutOrderCanceled(Model model, Principal principal) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("orders", orderService.orderListFindByStatus(OrderStatus.CANCELED));
        return "orders";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_OWNER')")
    @GetMapping("/orders/closed")
    public String aboutOrderClosed(Model model, Principal principal) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("orders", orderService.orderListFindByStatus(OrderStatus.CLOSED));
        return "orders";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_OWNER')")
    @GetMapping("/orders/{id}")
    public String orderInfo(@PathVariable("id") Long id, Model model, Principal principal) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("order", orderService.getOrderById(id));
        return "order-info";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_USER') || hasAuthority('ROLE_OWNER')")
    @PostMapping("/orders/edit/{id}")
    public String updateOrderById(@PathVariable("id") Long id,
                                  @RequestParam("status") OrderStatus status) {
        orderService.updateOrderStatusById(id, status);
        return "redirect:/";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_OWNER')")
    @PostMapping("/admin/orders/edit/{id}")
    public String updateOrderByIdByAdmin(@PathVariable("id") Long id,
                              @RequestParam("status") OrderStatus status) {
        orderService.updateOrderStatusById(id, status);
        return "redirect:/orders/{id}";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_USER') || hasAuthority('ROLE_OWNER')")
    @GetMapping("/orders/user/{id}")
    public String aboutUserOrder(@PathVariable("id") Long id, Model model, Principal principal) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("orders", orderService.getUserOrdersById(id));
        return "user-orders";
    }

}
