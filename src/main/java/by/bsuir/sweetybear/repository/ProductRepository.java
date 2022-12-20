package by.bsuir.sweetybear.repository;

import by.bsuir.sweetybear.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Modifying
    @Query(value = "SELECT * FROM products WHERE title LIKE ?1" + "%", nativeQuery = true)
    List<Product> findByTitle(String title);
}
