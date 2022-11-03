package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.model.Bucket;
import by.bsuir.sweetybear.model.Image;
import by.bsuir.sweetybear.model.Product;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.repository.ImageRepository;
import by.bsuir.sweetybear.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
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
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final UserService userService;
    private final BucketService bucketService;
    private final ImageRepository imageRepository;

    public List<Product> listProducts(String title) {
        if (title != null) return productRepository.findByTitle(title);
        return productRepository.findAll();
    }

    public void saveProduct(Product product, MultipartFile file1, MultipartFile file2) throws IOException {
        addImages(product, file1, file2);
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

    private void addImages(Product product, MultipartFile file1, MultipartFile file2) throws IOException {
        Image image1;
        Image image2;
        if (file1.getSize() != 0) {
            if (product.getImages() != null) {
                imageRepository.markToDeleteByProductId(product.getId(), "toDelete");
                imageRepository.deleteByName("toDelete");
                log.info("Delete product images.");
                product.setPreviewImageId(null);
                product.getImages().clear();
            }
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            product.addImageToProduct(image1);
        }
        if (file2.getSize() != 0) {
            image2 = toImageEntity(file2);
            product.addImageToProduct(image2);
        }
    }

    public void updateProductById(Long id, Product productUpdate, MultipartFile file1, MultipartFile file2) throws IOException {
        log.info("Update product. Id: {}", id);
        Product product = productRepository.findById(id).orElse(null);
        assert product != null;
        addImages(product, file1, file2);
        product.setTitle(productUpdate.getTitle());
        product.setDescription(productUpdate.getDescription());
        product.setPrice(productUpdate.getPrice());
        product.setWeight(productUpdate.getWeight());
        product.setAvailability(productUpdate.isAvailability());
        Product productFromDb = productRepository.save(product);
        product.setPreviewImageId(productFromDb.getImages().get(0).getId());
        productRepository.save(product);
    }

    @Transactional
    public void addToUserBucket(Long productId, String email) {
        User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new IllegalStateException("User not found. Email: " + email);
        }

        Bucket bucket = user.getBucket();
        if (bucket == null) {
            Bucket newBucket = bucketService.createBucket(user, Collections.singletonList(productId));
            user.setBucket(newBucket);
            userService.save(user);
        } else {
            bucketService.addProducts(bucket, Collections.singletonList(productId));
        }
    }
}
