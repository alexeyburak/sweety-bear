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



}
