package by.bsuir.sweetybear.repository;

import by.bsuir.sweetybear.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByTitle(String title);
}
