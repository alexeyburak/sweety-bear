package by.bsuir.sweetybear.dto;

import by.bsuir.sweetybear.model.Address;
import by.bsuir.sweetybear.model.OrderDetails;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.model.enums.DeliveryType;
import by.bsuir.sweetybear.model.enums.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderViewingDTO implements Comparable<OrderViewingDTO> {
    Long id;
    User user;
    BigDecimal sum;
    Address address;
    List<OrderDetails> details;
    DeliveryType delivery;
    OrderStatus status;
    LocalDateTime dateOfCreated;
    LocalDateTime dateOfDelivery;
    boolean isOrderPaid;

    public boolean isOrderDeliveryPickup() {
        return delivery == DeliveryType.PICKUP;
    }

    public boolean isOrderCanceled() {
        return status == OrderStatus.CANCELED || status == OrderStatus.CLOSED;
    }

    public boolean isOrderApproved() {
        return status == OrderStatus.APPROVED;
    }

    public boolean isTotalProductsPriceNotChanged() {
        return sum.compareTo(BigDecimal.valueOf(countCurrentlyProductsPrice())) == 0;
    }

    public int getNumberOfDaysOfTheOrderPaymentDay() {
        return dateOfDelivery.compareTo(LocalDateTime.now());
    }

    private Double countCurrentlyProductsPrice() {
        return details
                .stream()
                .map(OrderDetails::getProductPriceWithAmount)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();
    }

    @Override
    public int compareTo(OrderViewingDTO o) {
        return status.compareTo(o.status);
    }
}
