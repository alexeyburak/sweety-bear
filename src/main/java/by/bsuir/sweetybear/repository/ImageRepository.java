package by.bsuir.sweetybear.repository;

import by.bsuir.sweetybear.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Modifying
    @Query(value = "UPDATE images SET name = ?2 where user_id = ?1", nativeQuery = true)
    void markToDeleteByUserId(Long id, String task);@Modifying
    @Query(value = "UPDATE images SET name = ?2 where product_id = ?1", nativeQuery = true)
    void markToDeleteByProductId(Long id, String task);
    void deleteByName(String name);
}
