package by.bsuir.sweetybear.repository;

import by.bsuir.sweetybear.model.Order;
import by.bsuir.sweetybear.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Nov 2022
 */

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByUserId(Long id);
}
