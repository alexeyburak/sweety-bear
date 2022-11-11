package by.bsuir.sweetybear.repository;

import by.bsuir.sweetybear.model.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Nov 2022
 */

public interface BucketRepository extends JpaRepository<Bucket, Long> {
    @Modifying
    @Query(value = "DELETE FROM buckets_products WHERE product_id = ?1", nativeQuery = true)
    void deleteByProductId(Long id);
}
