package by.bsuir.sweetybear.dto.product;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRatingDTO {
    int allRatings;
    double avgRating;
    Map<Integer, Double> starsWithPercentages;
}



