package by.bsuir.sweetybear.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "buckets")
@Data
public class Bucket extends IdentifiedModel{

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "buckets_products",
    joinColumns = @JoinColumn(name = "bucket_id"),
    inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products;

    public int getNumberOfProductsInBucket() {
        return  products.size();
    }
}
