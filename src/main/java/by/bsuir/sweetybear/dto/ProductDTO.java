package by.bsuir.sweetybear.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Nov 2022
 */

@Data
@NoArgsConstructor
public class ProductDTO {

    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    @NotEmpty(message = "Title is required")
    private String title;
    @Length(min = 5, max = 300, message = "Description must be between 5 and 300 characters")
    @NotEmpty(message = "Description is required")
    private String description;
    @NotNull(message = "It's required")
    @Digits(integer = 8, fraction = 2)
    @Positive(message = "Should be positive")
    private Double price;
    @NotNull(message = "It's required")
    @Min(value = 10, message = "Minimum of 10")
    private int weight;
    @NotNull(message = "Availability is required")
    private boolean availability;
}
