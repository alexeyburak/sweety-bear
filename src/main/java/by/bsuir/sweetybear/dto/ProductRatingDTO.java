package by.bsuir.sweetybear.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class ProductRatingDTO {
    private int allRatings;
    private double avgRating;
    private Map<Integer, Double> starsWithPercentages;
}



