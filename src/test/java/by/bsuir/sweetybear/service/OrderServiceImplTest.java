package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.model.Order;
import by.bsuir.sweetybear.model.enums.OrderStatus;
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

import java.util.Optional;

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
    void save_Order_ShouldSaveOrderToDatabase() {
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

    @Test
    void getOrderById_OrderId_ShouldFindOrderById() {
        // given
        final long id = 1L;
        Order order = new Order();
        order.setId(id);

        //when
        Mockito.when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        Order result = orderService.getOrderById(id);

        //then
        Assertions.assertNotNull(result);
        AssertionsForClassTypes.assertThat(result).isEqualTo(order);
        Assertions.assertEquals(result.getId(), order.getId());
    }

    @Test
    void updateOrderStatusById_OrderIdStatus_ShouldChangeOrderStatus() {
        //given
        final long id = 1L;
        Order order = Order.builder()
                .status(OrderStatus.NEW)
                .build();

        //when
        Mockito.when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        orderService.updateOrderStatusById(id, OrderStatus.APPROVED);

        //then
        Assertions.assertNotNull(order);
        Assertions.assertNotNull(order.getStatus());
        Assertions.assertEquals(order.getStatus(), OrderStatus.APPROVED);
    }

}
