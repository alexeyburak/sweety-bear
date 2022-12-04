package by.bsuir.sweetybear.model;

import by.bsuir.sweetybear.model.enums.DeliveryType;
import by.bsuir.sweetybear.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Nov 2022
 */

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order extends IdentifiedModel implements Comparable<Order>{

    private static final Long DELIVERY_TIME = 5L;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "sum")
    private BigDecimal sum;
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "details_id")
    private List<OrderDetails> details;
    @Enumerated(EnumType.STRING)
    private DeliveryType delivery;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private LocalDateTime dateOfCreated;
    private LocalDateTime dateOfDelivery;

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

    private Double countCurrentlyProductsPrice() {
        return details
                .stream()
                .map(OrderDetails::getProductPriceWithAmount)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();
    }

    @PrePersist
    private void init() {
        dateOfCreated = LocalDateTime.now();
        dateOfDelivery = LocalDateTime.now().plusDays(DELIVERY_TIME);
    }

    @Override
    public int compareTo(Order o) {
        if(o.status.ordinal() < this.status.ordinal())
            return 1;
        else if(o.status.ordinal() > this.status.ordinal())
            return -1;
        else
            return 1;
    }
}
