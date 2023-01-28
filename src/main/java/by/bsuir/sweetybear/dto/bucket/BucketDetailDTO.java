package by.bsuir.sweetybear.dto.bucket;

import by.bsuir.sweetybear.model.Product;
import lombok.*;

import java.math.BigDecimal;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Nov 2022
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BucketDetailDTO {

    private String title;
    private Long productId;
    private Double price;
    private BigDecimal amount;
    private Double sum;

    public BucketDetailDTO(Product product) {
        this.title = product.getTitle();
        this.productId = product.getId();
        this.price = product.getPrice();
        this.amount = new BigDecimal("1.0");
        this.sum = Double.valueOf(product.getPrice().toString());
    }
}
