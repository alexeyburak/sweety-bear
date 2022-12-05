package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.model.Order;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.model.enums.OrderStatus;
import by.bsuir.sweetybear.service.OrderServiceImpl;
import by.bsuir.sweetybear.service.PDFGeneratorServiceImpl;
import by.bsuir.sweetybear.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Objects;

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
    private final PDFGeneratorServiceImpl pdfGeneratorService;

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
        User userFromPrincipal = userService.getUserByPrincipal(principal);

        if (!Objects.equals(id, userFromPrincipal.getId()))
            return "redirect:/";

        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("orders", orderService.getUserOrdersById(id));
        return "user-orders";
    }

    @GetMapping("/order/pdf/export/{id}")
    public void generatePDF(@PathVariable("id") Long id,
                            HttpServletResponse response,
                            Principal principal) {
        Order order = orderService.getOrderById(id);
        User user = userService.getUserByPrincipal(principal);
        if (!user.equals(order.getUser())) {
            return;
        }

        response.setContentType("application/pdf");
        String headerValue = "attachment; filename=order_" + id + ".pdf";
        response.setHeader("Content-Disposition", headerValue);

        pdfGeneratorService.exportUserOrderInPDF(response, id);
    }

}
