package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.model.Order;
import by.bsuir.sweetybear.model.enums.OrderStatus;
import by.bsuir.sweetybear.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public List<Order> orderList(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void updateOrderStatusById(Long id, OrderStatus status) {
        Order order = this.getOrderById(id);
        order.setStatus(status);
        orderRepository.save(order);
    }
}