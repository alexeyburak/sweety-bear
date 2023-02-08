package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.model.Order;
import by.bsuir.sweetybear.model.enums.OrderStatus;
import by.bsuir.sweetybear.repository.OrderRepository;
import by.bsuir.sweetybear.service.impl.BankCardServiceImpl;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Feb 2023
 */

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {
    @Mock
    private BankCardServiceImpl bankCardService;
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

    @Test
    void checkForOrderPaymentDate_UserId_ShouldCancelOrderWhenPaymentDateIsExpired() {
        //given
        final long userId = 1L;
        List<Order> orders = getUserOrdersList();

        //when
        Mockito.when(orderRepository.findByUserId(userId)).thenReturn(orders);
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(orders.get(0)));
        orderService.checkForOrderPaymentDate(userId);

        //then
        Assertions.assertNotNull(orders);
        Assertions.assertEquals(orders.get(0).getStatus(), OrderStatus.CANCELED);
        Assertions.assertEquals(orders.get(1).getStatus(), OrderStatus.APPROVED);
    }

    private List<Order> getUserOrdersList() {
        Order orderToChange = Order.builder()
                .status(OrderStatus.NEW)
                .dateOfDelivery(LocalDateTime.now().minusDays(1))
                .build();
        orderToChange.setId(1L);

        Order orderValid = Order.builder()
                .status(OrderStatus.APPROVED)
                .dateOfDelivery(LocalDateTime.now().plusDays(1))
                .build();
        return List.of(orderToChange,
                orderValid);
    }

    @Test
    void orderPayment_ValidPaymentId_TrueAndMarkOrderPaid() {
        //given
        final long orderId = 1L;
        final long paymentId = 1L;
        Order order = Order.builder()
                .isOrderPaid(false)
                .build();

        //when
        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(bankCardService.debitingMoneyFromTheBankCard(order, paymentId)).thenReturn(true);
        boolean result = orderService.orderPayment(orderId, paymentId);

        //then
        Assertions.assertNotNull(order);
        Assertions.assertTrue(order.isOrderPaid());
        Assertions.assertTrue(result);
    }

    @Test
    void orderPayment_NotValidPaymentId_False() {
        //given
        final long orderId = 1L;
        final long paymentId = 1L;
        Order order = Order.builder()
                .isOrderPaid(false)
                .build();

        //when
        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(bankCardService.debitingMoneyFromTheBankCard(order, paymentId)).thenReturn(false);
        boolean result = orderService.orderPayment(orderId, paymentId);

        //then
        Assertions.assertNotNull(order);
        Assertions.assertFalse(order.isOrderPaid());
        Assertions.assertFalse(result);
    }

}
