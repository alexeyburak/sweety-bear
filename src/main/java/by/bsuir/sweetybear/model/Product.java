package by.bsuir.sweetybear.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
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
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    @NotEmpty(message = "Title is required")
    private String title;
    @Column(name = "description", columnDefinition = "text")
    @Length(min = 5, max = 300, message = "Description must be between 5 and 300 characters")
    @NotEmpty(message = "Description is required")
    private String description;
    @Column(name = "price")
    @NotNull(message = "It's required")
    @Digits(integer = 8, fraction = 2)
    @Positive(message = "Should be positive")
    private Double price;
    @Column(name = "Weight")
    @NotNull(message = "It's required")
    @Min(value = 10, message = "Minimum of 10")
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

    public boolean isOnlyOneImage() {
        return images.size() == 1;
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
