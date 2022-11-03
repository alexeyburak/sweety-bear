package by.bsuir.sweetybear.repository;

import by.bsuir.sweetybear.model.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Nov 2022
 */

public interface BucketRepository extends JpaRepository<Bucket, Long> {
}
