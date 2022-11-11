package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.exception.ApiRequestException;
import by.bsuir.sweetybear.model.Order;
import by.bsuir.sweetybear.model.enums.OrderStatus;
import by.bsuir.sweetybear.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Nov 2022
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public void saveOrder(Order order) {
        log.info("Save order. Id: {}", order.getId());
        orderRepository.save(order);
    }

    @Override
    public List<Order> orderList(OrderStatus status) {
        return orderRepository
                .findByStatus(status)
                .stream()
                .sorted(Comparator.comparing(Order::getDateOfCreated)
                        .reversed())
                .toList();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    public void updateOrderStatusById(Long id,
                                      OrderStatus status) {
        Order order = this.getOrderById(id);
        if (order == null)
            throw new ApiRequestException("Order not found");
        log.info("Change order status. Id: {}. Status: {}. New status: {}", id, order.getStatus(), status);
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Override
    public List<Order> getUserOrders(Long id) {
        return orderRepository
                .findByUserId(id)
                .stream()
                .sorted(Order::compareTo)
                .toList();
    }

    public void deleteProduct(Long id) {
        log.warn("Delete product from order. Product id: {}", id);
        orderRepository.deleteByProductId(id);
    }
}
