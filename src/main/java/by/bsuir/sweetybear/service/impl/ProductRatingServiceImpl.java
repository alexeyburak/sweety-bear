package by.bsuir.sweetybear.service.impl;

import by.bsuir.sweetybear.dto.product.ProductRatingDTO;
import by.bsuir.sweetybear.model.Feedback;
import by.bsuir.sweetybear.service.ProductRatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductRatingServiceImpl implements ProductRatingService {

    private static final int MIN_RANGE_RATING = 1;
    private static final int MAX_RANGE_RATING = 6;
    private final FeedbackServiceImpl feedbackService;

    @Override
    public ProductRatingDTO generateProductRating(Long id) {
        List<Feedback> productFeedbacks = feedbackService.getProductFeedbackList(id);

        return productFeedbacks.isEmpty() ? new ProductRatingDTO() : ProductRatingDTO.builder()
                .allRatings(countAllRatings(productFeedbacks))
                .avgRating(countAvgRating(productFeedbacks))
                .starsWithPercentages(countStarsWithPercentages(productFeedbacks))
                .build();
    }

    @Override
    public int countAllRatings(List<Feedback> productFeedbacks) {
        return productFeedbacks.size();
    }

    @Override
    public double countAvgRating(List<Feedback> productFeedbacks) {
        return productFeedbacks.stream()
                .mapToDouble(Feedback::getStars)
                .average()
                .orElse(Double.NaN);
    }

    @Override
    public Map<Integer, Double> countStarsWithPercentages(List<Feedback> list) {
        Map<Integer, Double> map = generateMapWithStarsRange();
        countNumberOfStarsInFeedback(list, map);
        calculateStarsPercents(list.size(), map);
        return map;
    }

    private Map<Integer, Double> generateMapWithStarsRange() {
        Map<Integer, Double> map = new HashMap<>();

        IntStream.range(MIN_RANGE_RATING, MAX_RANGE_RATING)
                .forEach(i -> map.put(i, 0.));
        return map;
    }

    private void countNumberOfStarsInFeedback(List<Feedback> list, Map<Integer, Double> map) {
        list.forEach(feedback ->
                map.put(feedback.getStars(), map.getOrDefault(feedback.getStars(), 0.) + 1));
    }

    private void calculateStarsPercents(int numberOfFeedbacks, Map<Integer, Double> map) {
        map.forEach((k, v) ->
                map.put(k, Math.round(v / numberOfFeedbacks * 1000) / 10.));
    }

}
