package by.bsuir.sweetybear.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackDTO {
    @NotEmpty(message = "Description is required")
    @Length(min = 10, max = 300, message = "Description must be between 10 and 300 characters")
    String description;
    @NotEmpty(message = "Title is required")
    @Length(min = 3, max = 20, message = "Title must be between 3 and 20 characters")
    String title;
    @Min(value = 1, message = "Min is 1")
    @Max(value = 5, message = "Max is 5")
    byte stars;
}
