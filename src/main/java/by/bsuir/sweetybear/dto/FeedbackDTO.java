package by.bsuir.sweetybear.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackDTO {
    @Length(min = 10, max = 300, message = "Description must be between 10 and 300 characters")
    String description;
    @Length(min = 3, max = 20, message = "Title must be between 3 and 20 characters")
    String title;
    @Max(value = 5, message = "Max is 5")
    byte stars;
}
