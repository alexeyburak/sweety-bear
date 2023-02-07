package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.model.Order;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.model.enums.Role;
import by.bsuir.sweetybear.repository.OrderRepository;
import by.bsuir.sweetybear.service.impl.OrderServiceImpl;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;

import static by.bsuir.sweetybear.utils.UUIDGenerator.generateUUID;
import static org.mockito.Mockito.doNothing;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Feb 2023
 */

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void addUserToDatabase_NotExistsUser_True() {
        // given
        Order order = Order.builder()
                .isOrderPaid(true)
                .build();

        //when
        orderService.save(order);

        //then
        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        Mockito.verify(orderRepository, Mockito.times(1)).save(orderArgumentCaptor.capture());
        Order capturedOrder = orderArgumentCaptor.getValue();

        Assertions.assertNotNull(capturedOrder);
        AssertionsForClassTypes.assertThat(capturedOrder).isEqualTo(order);
    }
}
