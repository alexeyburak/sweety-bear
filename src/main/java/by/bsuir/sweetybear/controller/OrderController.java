package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.dto.BankCardDTO;
import by.bsuir.sweetybear.model.Order;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.model.enums.OrderStatus;
import by.bsuir.sweetybear.service.impl.OrderServiceImpl;
import by.bsuir.sweetybear.service.impl.PDFGeneratorServiceImpl;
import by.bsuir.sweetybear.service.impl.UserServiceImpl;
import by.bsuir.sweetybear.validator.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Objects;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Controller
@RequiredArgsConstructor
public class OrderController {

    private static final String PDF_CONTENT_TYPE = "application/pdf";
    private static final String PDF_HEADER_KEY = "Content-Disposition";

    private final OrderServiceImpl orderService;
    private final UserServiceImpl userService;
    private final PDFGeneratorServiceImpl pdfGeneratorService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_OWNER')")
    @GetMapping("/orders")
    public String aboutOrders(@RequestParam("status") OrderStatus orderStatus,
                              Model model,
                              Principal principal) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("orders", orderService.orderListFindByStatus(orderStatus));
        return "orders";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_OWNER')")
    @GetMapping("/orders/{id}")
    public String orderInfo(@PathVariable("id") Long id,
                            Model model,
                            Principal principal) {
        Order order = orderService.getOrderById(id);
        orderService.checkForOrderPaymentDate(order.getUser().getId());

        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("order", order);
        return "order-info";
    }

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

    @GetMapping("/orders/user/{id}")
    public String aboutUserOrder(@PathVariable("id") Long id,
                                 Model model,
                                 Principal principal) {
        orderService.checkForOrderPaymentDate(id);

        User userFromPrincipal = userService.getUserByPrincipal(principal);
        if (!userFromPrincipal.isActive())
            return "redirect:/login";
        if (!Objects.equals(id, userFromPrincipal.getId()))
            return "redirect:/";

        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("orders", orderService.getUserOrdersById(id));
        return "user-orders";
    }

    @GetMapping("/payment/orders/{id}")
    public String orderPayment(@PathVariable("id") Long id,
                                   @ModelAttribute("bankCard") BankCardDTO bankCard,
                                   Model model,
                                   Principal principal) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("order", orderService.getOrderById(id));
        return "order-payment";
    }

    @PostMapping("/payment/orders/{id}")
    public String makeOrderPayment(@PathVariable("id") Long id,
                                   @ModelAttribute("bankCard") BankCardDTO bankCard,
                                   Model model,
                                   Principal principal) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("order", orderService.getOrderById(id));

        if (!orderService.orderPayment(id, bankCard)) {
            model.addAttribute("report", ErrorMessage.ORDER_PAYMENT_ERROR);
            return "order-payment";
        }

        return "redirect:/";
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

        response.setContentType(PDF_CONTENT_TYPE);
        String headerValue = "attachment; filename=order_" + id + ".pdf";
        response.setHeader(PDF_HEADER_KEY, headerValue);

        pdfGeneratorService.exportUserOrderInPDF(response, id);
    }

}
