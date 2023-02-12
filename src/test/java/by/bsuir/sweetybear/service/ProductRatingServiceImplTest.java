package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.model.Feedback;
import by.bsuir.sweetybear.service.impl.FeedbackServiceImpl;
import by.bsuir.sweetybear.service.impl.ProductRatingServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Feb 2023
 */

@ExtendWith(MockitoExtension.class)
public class ProductRatingServiceImplTest {
    @Mock
    private FeedbackServiceImpl feedbackService;
    @InjectMocks
    private ProductRatingServiceImpl productRatingService;

    @Test
    void countAllRatings_FeedbackList_ReturnSize() {
        //given
        List<Feedback> feedbacks = new ArrayList<>(
                List.of(
                        new Feedback(),
                        new Feedback(),
                        new Feedback(),
                        new Feedback(),
                        new Feedback()
                )
        );
        List<Feedback> emptyFeedbacks = new ArrayList<>();

        //when
        int result = productRatingService.countAllRatings(feedbacks);
        int emptyResult = productRatingService.countAllRatings(emptyFeedbacks);

        //then
        Assertions.assertEquals(5, result);
        Assertions.assertEquals(0, emptyResult);
    }

    @Test
    void countAvgRatings_FeedbackList_ReturnAvgValue() {
        //given
        List<Feedback> feedbacks = new ArrayList<>(
                List.of(
                        Feedback.builder().stars(2).build(),
                        Feedback.builder().stars(4).build(),
                        Feedback.builder().stars(4).build(),
                        Feedback.builder().stars(5).build()
                )
        );
        List<Feedback> feedbacks2 = new ArrayList<>(
                List.of(
                        Feedback.builder().stars(5).build(),
                        Feedback.builder().stars(5).build()
                )
        );

        //when
        double result = productRatingService.countAvgRating(feedbacks);
        double result2 = productRatingService.countAvgRating(feedbacks2);

        //then
        Assertions.assertEquals(3.75, result);
        Assertions.assertEquals(5, result2);
    }

    @Test
    void countStarsWithPercentages_FeedbackList_ReturnStarsWithPercentagesMap() {
        //given
        List<Feedback> feedbacks = List.of(
                Feedback.builder().stars(1).build(),
                Feedback.builder().stars(2).build(),
                Feedback.builder().stars(3).build(),
                Feedback.builder().stars(4).build(),
                Feedback.builder().stars(5).build()

        );
        Map<Integer, Double> expected = Map.of(
                1, 20.0,
                2, 20.0,
                3, 20.0,
                4, 20.0,
                5, 20.0
        );
        List<Feedback> feedbacks2 = List.of(
                Feedback.builder().stars(5).build(),
                Feedback.builder().stars(5).build()

        );
        Map<Integer, Double> expected2 = Map.of(
                1, 0.0,
                2, 0.0,
                3, 0.0,
                4, 0.0,
                5, 100.0
        );

        //when
        Map<Integer, Double> result = productRatingService.countStarsWithPercentages(feedbacks);
        Map<Integer, Double> result2 = productRatingService.countStarsWithPercentages(feedbacks2);

        //then
        Assertions.assertEquals(5, result.size());
        Assertions.assertEquals(expected, result);

        Assertions.assertEquals(5, result2.size());
        Assertions.assertEquals(expected2, result2);
    }
}
