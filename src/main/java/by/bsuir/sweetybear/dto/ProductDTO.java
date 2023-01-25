package by.bsuir.sweetybear.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO {
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    @NotEmpty(message = "Title is required")
    String title;
    @Length(min = 5, max = 300, message = "Description must be between 5 and 300 characters")
    @NotEmpty(message = "Description is required")
    String description;
    @NotNull(message = "It's required")
    @Digits(integer = 8, fraction = 2, message = "It's too much")
    @Positive(message = "Should be positive")
    Double price;
    @NotNull(message = "It's required")
    @Min(value = 10, message = "Minimum of 10")
    int weight;
    @NotNull(message = "Availability is required")
    boolean availability;
}
