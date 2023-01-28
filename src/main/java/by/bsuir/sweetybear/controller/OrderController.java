package by.bsuir.sweetybear.controller;

import by.bsuir.sweetybear.dto.OrderViewingDTO;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.model.enums.OrderStatus;
import by.bsuir.sweetybear.service.impl.BankCardServiceImpl;
import by.bsuir.sweetybear.service.impl.OrderServiceImpl;
import by.bsuir.sweetybear.service.impl.PDFGeneratorServiceImpl;
import by.bsuir.sweetybear.service.impl.UserServiceImpl;
import by.bsuir.sweetybear.validator.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
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
    private final BankCardServiceImpl bankCardService;
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
        OrderViewingDTO order = orderService.getOrderViewingDTOById(id);
        orderService.checkForOrderPaymentDate(order.getUser().getId());

        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("order", order);
        return "order-info";
    }

    @PostMapping("/orders/edit/{id}")
    public String updateOrderById(@PathVariable("id") Long id,
                                  @RequestParam("status") OrderStatus status,
                                  HttpServletRequest request) {
        orderService.updateOrderStatusById(id, status);
        return "redirect:" + request.getHeader("Referer");
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
                                 Principal principal,
                                 @RequestParam(name = "tab") String tab) {
        orderService.checkForOrderPaymentDate(id);

        User userFromPrincipal = userService.getUserByPrincipal(principal);
        if (!Objects.equals(id, userFromPrincipal.getId()))
            return "redirect:/";

        model.addAttribute("user", userFromPrincipal);
        model.addAttribute("orders", orderService.getUserOrdersByIdWithStatus(id, tab));
        return "user-orders";
    }

    @GetMapping("/payment/orders/{id}")
    public String orderPayment(@PathVariable("id") Long id,
                               Model model,
                               Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        addDataToModelInOrderPayment(model, id, user);
        return "order-payment";
    }

    @PostMapping("/payment/orders/{id}")
    public String makeOrderPayment(@PathVariable("id") Long id,
                                   @RequestParam("payment") Long paymentId,
                                   Model model,
                                   Principal principal) {
        User user = userService.getUserByPrincipal(principal);

        if (!orderService.orderPayment(id, paymentId)) {
            addDataToModelInOrderPayment(model, id, user);
            model.addAttribute("report", ErrorMessage.ORDER_PAYMENT_ERROR);
            return "order-payment";
        }

        return "redirect:/orders/user/" + user.getId() + "?tab=waiting";
    }

    private void addDataToModelInOrderPayment(Model model, Long id, User user) {
        model.addAttribute("user", user);
        model.addAttribute("payments", bankCardService.getBankCardDTOListByUserId(user.getId()));
        model.addAttribute("order", orderService.getOrderViewingDTOById(id));
    }

    @PostMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrderById(id);
        return "redirect:/orders?status=NEW";
    }

    @GetMapping("/order/pdf/export/{id}")
    public void generatePDF(@PathVariable("id") Long id,
                            HttpServletResponse response,
                            Principal principal) {
        User user = userService.getUserByPrincipal(principal);

        response.setContentType(PDF_CONTENT_TYPE);
        response.setHeader(PDF_HEADER_KEY, "attachment; filename=order_" + id + ".pdf");

        pdfGeneratorService.exportUserOrderInPDF(response, id);
    }

}
