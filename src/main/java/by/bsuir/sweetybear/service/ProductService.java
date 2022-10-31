package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.model.Image;
import by.bsuir.sweetybear.model.Product;
import by.bsuir.sweetybear.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static by.bsuir.sweetybear.utils.Utils.toImageEntity;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> listProducts(String title) {
        if (title != null) return productRepository.findByTitle(title);
        return productRepository.findAll();
    }

    public void saveProduct(Product product, MultipartFile file1, MultipartFile file2) throws IOException {
        Image image1;
        Image image2;
        if (file1.getSize() != 0) {
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            product.addImageToProduct(image1);
        }
        if (file2.getSize() != 0) {
            image2 = toImageEntity(file2);
            product.addImageToProduct(image2);
        }
        log.info("Saving new Product. Id: {}, Title: {} ", product.getId(), product.getTitle());
        Product productFromDb = productRepository.save(product);
        productFromDb.setPreviewImageId(productFromDb.getImages().get(0).getId());
        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        log.info("Delete product. Id: {}", id);
        productRepository.deleteById(id);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void updateProductById(Long id, Product productUpdate) {
        log.info("Update product. Id: {}", id);
        Product product = productRepository.findById(id).orElse(null);
        assert product != null;
        product.setTitle(productUpdate.getTitle());
        product.setDescription(productUpdate.getDescription());
        product.setPrice(productUpdate.getPrice());
        product.setWeight(productUpdate.getWeight());
        product.setAvailability(productUpdate.isAvailability());
        productRepository.save(product);
    }
}
