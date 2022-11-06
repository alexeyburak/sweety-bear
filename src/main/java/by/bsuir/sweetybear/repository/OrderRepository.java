package by.bsuir.sweetybear.repository;

import by.bsuir.sweetybear.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Nov 2022
 */

public interface OrderRepository extends JpaRepository<Order, Long> {
}
