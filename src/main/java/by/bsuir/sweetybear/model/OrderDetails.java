package by.bsuir.sweetybear.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Nov 2022
 */

@Entity
@Table(name = "orders_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetails extends IdentifiedModel {

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "price")
    private BigDecimal price;

}
