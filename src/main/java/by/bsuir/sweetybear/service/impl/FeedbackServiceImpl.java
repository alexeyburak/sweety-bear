package by.bsuir.sweetybear.service.impl;

import by.bsuir.sweetybear.model.Feedback;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.model.enums.FeedbackSortType;
import by.bsuir.sweetybear.repository.FeedbackRepository;
import by.bsuir.sweetybear.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
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
    public List<Feedback> getUserFeedbackList(long userId) {
        return feedbackRepository.getFeedbacksByUserId(userId);
    }

    @Override
    public List<Feedback> getProductFeedbackList(long id) {
        return feedbackRepository.getFeedbacksByProductId(id);
    }

    @Override
    public List<Feedback> getProductFeedbackListWithSortingType(long id, FeedbackSortType sortType) {
        final List<Feedback> feedbacksByProductId = this.getProductFeedbackList(id);

        if (sortType != null) {
            switch (sortType) {
                case RATING_ASC -> feedbacksByProductId
                        .sort(Comparator.comparing(Feedback::getStars));
                case RATING_DESC -> feedbacksByProductId
                        .sort(Comparator.comparing(Feedback::getStars).reversed());
                case DATE_ASC -> feedbacksByProductId
                        .sort(Comparator.comparing(Feedback::getDateOfCreated));
                case DATE_DESC -> feedbacksByProductId
                        .sort(Comparator.comparing(Feedback::getDateOfCreated).reversed());
            }
        }

        return feedbacksByProductId;
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
    @Transactional
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteByFeedbackId(id);
        log.info("Delete feedback. Id: {}", id);
    }
}
