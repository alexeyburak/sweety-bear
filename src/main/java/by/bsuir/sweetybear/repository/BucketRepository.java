package by.bsuir.sweetybear.repository;

import by.bsuir.sweetybear.model.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BucketRepository extends JpaRepository<Bucket, Long> {
    @Modifying
    @Query(value = "DELETE FROM buckets_products WHERE product_id = ?1", nativeQuery = true)
    void deleteByProductId(Long id);
    @Modifying
    @Query(value = "UPDATE buckets SET user_id = null WHERE user_id = ?1", nativeQuery = true)
    void deleteByUserId(Long id);
}
