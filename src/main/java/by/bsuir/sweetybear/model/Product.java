package by.bsuir.sweetybear.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product extends IdentifiedModel {

    @Column(name = "title")
    @Size(min = 3, max = 30, message = "title must be between 3 and 30 characters")
    @NotEmpty(message = "title is required")
    private String title;
    @Column(name = "description", columnDefinition = "text")
    @Length(min = 5, max = 1000, message = "description must be between 5 and 1000 characters")
    @NotEmpty(message = "description is required")
    private String description;
    @Column(name = "price")
    @NotNull(message = "price is required")
    private BigDecimal price;
    @Column(name = "weight")
    @NotNull(message = "weight is required")
    @Min(value = 10, message = "weight must be min 10")
    private int weight;
    @Column(name = "availability")
    @NotNull(message = "availability is required")
    private boolean availability;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    @NotNull(message = "image is required")
    private List<Image> images = new ArrayList<>();
    private Long previewImageId;
    private LocalDateTime dateOfCreated;

    public boolean isProductAvailable() {
        return availability;
    }

    @PrePersist
    private void init() {
        dateOfCreated = LocalDateTime.now();
    }

    public void addImageToProduct(Image image) {
        image.setProduct(this);
        images.add(image);
    }
}
