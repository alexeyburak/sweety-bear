package by.bsuir.sweetybear.service.impl;

import by.bsuir.sweetybear.model.Feedback;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.repository.FeedbackRepository;
import by.bsuir.sweetybear.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ProductServiceImpl productService;

    @Override
    public List<Feedback> getProductFeedbackList(long productId) {
        return feedbackRepository.getFeedbacksByProductId(productId)
                .stream()
                .sorted(Feedback::compareTo)
                .toList();
    }

    @Override
    public void addFeedback(User user, Long productId, Feedback feedback) {
        feedbackRepository.save(
                Feedback.builder()
                        .user(user)
                        .product(productService.getProductById(productId))
                        .description(feedback.getDescription())
                        .title(feedback.getTitle())
                        .stars(feedback.getStars())
                        .build()
        );
        log.info("Create new feedback. Product id: {}", productId);
    }

    @Override
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
        log.info("Delete feedback.");
    }
}
