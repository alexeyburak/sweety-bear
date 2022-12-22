package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.dto.BankCardDTO;
import by.bsuir.sweetybear.model.Order;
import by.bsuir.sweetybear.model.enums.OrderStatus;

import java.util.List;

public interface OrderService {

    // Save order to database
    void save(Order order);
    // Return orders with special status
    List<Order> orderListFindByStatus(OrderStatus status);
    // Find order by id
    Order getOrderById(Long id);
    // Update order status
    void updateOrderStatusById(Long id, OrderStatus status);
    // Return user orders
    List<Order> getUserOrdersById(Long id);
    // Delete product from database
    void deleteProductById(Long id);
    boolean orderPayment(Long orderId, BankCardDTO bankCardDTO);
    void checkForOrderPaymentDate(Long userId);
}
