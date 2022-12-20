package by.bsuir.sweetybear.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "orders_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetails extends IdentifiedModel {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "price")
    private BigDecimal price;

    public OrderDetails(Order order, Product product, Long amount) {
        this.order = order;
        this.product = product;
        this.amount = new BigDecimal(amount);
        this.price = new BigDecimal(String.valueOf(product.getPrice()));
    }

    public BigDecimal getProductPriceWithAmount() {
        return price.multiply(amount);
    }

}
