package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.dto.order.OrderViewingDTO;
import by.bsuir.sweetybear.model.Order;
import by.bsuir.sweetybear.model.enums.OrderStatus;

import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

public interface OrderService {
    OrderViewingDTO getOrderViewingDTOById(Long id);
    List<OrderViewingDTO> orderListFindByStatus(OrderStatus status);
    List<OrderViewingDTO> getUserOrdersByIdWithStatus(Long id, String status);
    Order getOrderById(Long id);
    boolean orderPayment(Long orderId, Long paymentId);
    void updateOrderStatusById(Long id, OrderStatus status);
    void checkForOrderPaymentDate(Long userId);
    void save(Order order);
    void deleteProductFromOrdersById(Long id);
    void deleteOrderById(Long id);
}
