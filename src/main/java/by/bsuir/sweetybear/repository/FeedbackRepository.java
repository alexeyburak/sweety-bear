package by.bsuir.sweetybear.repository;

import by.bsuir.sweetybear.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> getFeedbacksByProductId(long productId);
    List<Feedback> getFeedbacksByUserId(long userId);
    @Modifying
    @Query(value = "DELETE FROM feedbacks WHERE id = ?1", nativeQuery = true)
    void deleteByFeedbackId(long productId);
}
