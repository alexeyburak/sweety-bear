package by.bsuir.sweetybear.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Nov 2022
 */

@Data
@NoArgsConstructor
public class ProductDTO {

    @Size(min = 3, max = 30, message = "title must be between 3 and 30 characters")
    @NotEmpty(message = "Title is required")
    private String title;
    @Length(min = 5, max = 1000, message = "Description must be between 5 and 1000 characters")
    @NotEmpty(message = "Description is required")
    private String description;
    @NotNull(message = "Price is required")
    private BigDecimal price;
    @NotNull(message = "Weight is required")
    @Min(value = 10, message = "Weight must be min 10")
    private int weight;
    @NotNull(message = "Availability is required")
    private boolean availability;
}
