package by.bsuir.sweetybear.model;

import by.bsuir.sweetybear.model.enums.DeliveryType;
import by.bsuir.sweetybear.model.enums.OrderStatus;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order extends IdentifiedModel {
    private static final Long DELIVERY_TIME = 5L;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "sum")
    private BigDecimal sum;
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "details_id")
    private List<OrderDetails> details;
    @Column(name = "delivery")
    @Enumerated(EnumType.STRING)
    private DeliveryType delivery;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @Column(name = "date_of_created")
    private LocalDateTime dateOfCreated;
    @Column(name = "date_of_delivery")
    private LocalDateTime dateOfDelivery;
    @Column(name = "is_order_paid")
    private boolean isOrderPaid;

    @PrePersist
    private void init() {
        dateOfCreated = LocalDateTime.now();
        dateOfDelivery = LocalDateTime.now().plusDays(DELIVERY_TIME);
    }
}
