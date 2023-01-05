package by.bsuir.sweetybear.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Entity
@Table(name = "buckets")
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Bucket extends IdentifiedModel{

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "buckets_products",
    joinColumns = @JoinColumn(name = "bucket_id"),
    inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products;

    public int getNumberOfProductsInBucket() {
        return  products.size();
    }
}
