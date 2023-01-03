package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.model.Feedback;
import by.bsuir.sweetybear.model.User;

import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

public interface FeedbackService {

    List<Feedback> getProductFeedbackList(long productId);
    void addFeedback(User user, Long productId, Feedback feedback);
    void deleteFeedback(Long id);
}
