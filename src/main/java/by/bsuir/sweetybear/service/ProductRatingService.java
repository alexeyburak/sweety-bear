package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.dto.product.ProductRatingDTO;
import by.bsuir.sweetybear.model.Feedback;

import java.util.List;
import java.util.Map;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

public interface ProductRatingService {
    ProductRatingDTO generateProductRating(Long id);
    int countAllRatings(List<Feedback> productFeedbacks);
    double countAvgRating(List<Feedback> productFeedbacks);
    Map<Integer, Double> countStarsWithPercentages(List<Feedback> list);
}
