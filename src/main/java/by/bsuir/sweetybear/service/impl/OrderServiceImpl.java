package by.bsuir.sweetybear.service.impl;

import by.bsuir.sweetybear.exception.ApiRequestException;
import by.bsuir.sweetybear.model.Order;
import by.bsuir.sweetybear.model.enums.OrderStatus;
import by.bsuir.sweetybear.repository.OrderRepository;
import by.bsuir.sweetybear.service.OrderService;
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
    public void save(Order order) {
        log.info("Save order. Id: {}", order.getId());
        orderRepository.save(order);
    }

    @Override
    public List<Order> orderListFindByStatus(OrderStatus status) {
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
                .orElseThrow(() -> new ApiRequestException("Order not found. Id: " + id));
    }

    @Override
    public void updateOrderStatusById(Long id,
                                      OrderStatus status) {
        Order order = this.getOrderById(id);

        log.info("Change order status. Id: {}. Status: {}. New status: {}", id, order.getStatus(), status);
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Override
    public List<Order> getUserOrdersById(Long id) {
        return orderRepository
                .findByUserId(id)
                .stream()
                .sorted(Order::compareTo)
                .toList();
    }

    @Override
    public void deleteProductById(Long id) {
        log.warn("Delete product from order. Product id: {}", id);
        orderRepository.deleteByProductId(id);
    }
}
