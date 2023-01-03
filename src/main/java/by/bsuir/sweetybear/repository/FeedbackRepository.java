package by.bsuir.sweetybear.repository;

import by.bsuir.sweetybear.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> getFeedbacksByProductId(long productId);
}
