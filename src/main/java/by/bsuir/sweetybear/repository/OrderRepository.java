package by.bsuir.sweetybear.repository;

import by.bsuir.sweetybear.model.Order;
import by.bsuir.sweetybear.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByUserId(Long id);
    @Modifying
    @Query(value = "DELETE FROM orders_details WHERE product_id = ?1", nativeQuery = true)
    void deleteByProductId(Long id);
    @Modifying
    @Query(value = "UPDATE orders SET user_id = null WHERE user_id = ?1", nativeQuery = true)
    void deleteByUserId(Long id);
}
