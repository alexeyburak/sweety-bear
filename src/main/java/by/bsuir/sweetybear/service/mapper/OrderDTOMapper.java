package by.bsuir.sweetybear.service.mapper;

import by.bsuir.sweetybear.dto.order.OrderViewingDTO;
import by.bsuir.sweetybear.model.Order;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

@Service
public class OrderDTOMapper implements Function<Order, OrderViewingDTO> {
    @Override
    public OrderViewingDTO apply(Order order) {
        return OrderViewingDTO.builder()
                .id(order.getId())
                .user(order.getUser())
                .sum(order.getSum())
                .address(order.getAddress())
                .details(order.getDetails())
                .delivery(order.getDelivery())
                .status(order.getStatus())
                .dateOfCreated(order.getDateOfCreated())
                .dateOfDelivery(order.getDateOfDelivery())
                .isOrderPaid(order.isOrderPaid())
                .build();
    }
}
