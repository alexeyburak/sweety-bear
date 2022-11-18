package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.model.Order;
import by.bsuir.sweetybear.model.enums.OrderStatus;

import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Nov 2022
 */

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
}
