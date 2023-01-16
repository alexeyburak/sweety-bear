package by.bsuir.sweetybear.service.impl;

import by.bsuir.sweetybear.exception.ApiRequestException;
import by.bsuir.sweetybear.model.Order;
import by.bsuir.sweetybear.model.enums.OrderStatus;
import by.bsuir.sweetybear.repository.OrderRepository;
import by.bsuir.sweetybear.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final String ORDERS_WAITING_LIST = "waiting";
    private static final String ORDERS_PURCHASES = "purchases";
    private final OrderRepository orderRepository;
    private final BankCardServiceImpl bankCardService;

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
        return orderRepository.findByUserId(id);
    }

    @Override
    public List<Order> getUserOrdersByIdWithStatus(Long id, String status) {
        final List<Order> orders = this.getUserOrdersById(id)
                .stream()
                .sorted(Order::compareTo)
                .toList();

        return switch (status) {
            case ORDERS_WAITING_LIST -> orders.stream()
                    .filter(order -> order.getStatus().equals(OrderStatus.NEW) || order.getStatus().equals(OrderStatus.APPROVED))
                    .toList();
            case ORDERS_PURCHASES -> orders.stream()
                    .filter(order -> order.getStatus().equals(OrderStatus.CANCELED) || order.getStatus().equals(OrderStatus.CLOSED))
                    .toList();
            default -> orders;
        };
    }

    @Override
    public void deleteProductFromOrdersById(Long id) {
        log.warn("Delete product from order. Product id: {}", id);
        orderRepository.deleteByProductId(id);
    }

    @Override
    @Transactional
    public void deleteOrderById(Long id) {
        log.warn("Delete order. Order id: {}", id);
        orderRepository.deleteFromOrdersDetailsByOrderId(id);
        orderRepository.deleteByOrderId(id);
    }

    @Override
    public boolean orderPayment(Long orderId, Long paymentId) {
        Order order = this.getOrderById(orderId);

        if (!bankCardService.debitingMoneyFromTheBankCard(order, paymentId))
            return false;

        order.setOrderPaid(true);
        this.save(order);
        log.info("Order was paid successfully. Order id:  {}", orderId);

        return true;
    }

    @Override
    public void checkForOrderPaymentDate(Long userId) {
        this.getUserOrdersById(userId)
                .stream()
                .filter(order -> !order.isOrderCanceled())
                .filter(Order::isOrderPaymentDeprecated)
                .forEach(order -> {
                            log.info("Order payment was overdue.");
                            this.updateOrderStatusById(order.getId(), OrderStatus.CANCELED);
                        }
                );
    }
}
