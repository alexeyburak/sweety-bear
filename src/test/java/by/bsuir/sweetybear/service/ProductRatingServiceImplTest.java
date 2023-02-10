package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.service.impl.FeedbackServiceImpl;
import by.bsuir.sweetybear.service.impl.ProductRatingServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

}
