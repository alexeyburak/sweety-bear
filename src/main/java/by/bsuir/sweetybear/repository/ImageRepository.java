package by.bsuir.sweetybear.repository;

import by.bsuir.sweetybear.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

public interface ImageRepository extends JpaRepository<Image, Long> {
}
