package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.model.Order;
import by.bsuir.sweetybear.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
