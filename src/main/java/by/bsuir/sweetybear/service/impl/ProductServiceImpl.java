package by.bsuir.sweetybear.service.impl;

import by.bsuir.sweetybear.dto.product.ProductViewingDTO;
import by.bsuir.sweetybear.exception.ProductNotFoundException;
import by.bsuir.sweetybear.exception.UserNotFoundException;
import by.bsuir.sweetybear.model.Bucket;
import by.bsuir.sweetybear.model.Image;
import by.bsuir.sweetybear.model.Product;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.model.enums.ProductSortType;
import by.bsuir.sweetybear.repository.ImageRepository;
import by.bsuir.sweetybear.repository.ProductRepository;
import by.bsuir.sweetybear.service.ProductService;
import by.bsuir.sweetybear.service.mapper.ProductDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserServiceImpl userService;
    private final BucketServiceImpl bucketService;
    private final OrderServiceImpl orderService;
    private final ImageRepository imageRepository;
    private final ProductDTOMapper productDTOMapper;

    @Override
    public List<ProductViewingDTO> listProducts(String title, ProductSortType type) {
        if (type != null) return listProductsWithSortingType(type);
        if (title != null) return productRepository
                .findByTitle(title)
                .stream()
                .map(productDTOMapper)
                .toList();
        return productRepository
                .findAll()
                .stream()
                .map(productDTOMapper)
                .toList();
    }

    private List<ProductViewingDTO> listProductsWithSortingType(ProductSortType type) {
        final List<ProductViewingDTO> productList = new ArrayList<>(
                productRepository.findAll()
                        .stream()
                        .map(productDTOMapper)
                        .toList()
        );

        switch (type) {
            case ASCENDING -> productList
                    .sort(Comparator.comparing(ProductViewingDTO::getPrice));
            case REDUCING -> productList
                    .sort(Comparator.comparing(ProductViewingDTO::getPrice)
                            .reversed());
            case NEW -> productList
                    .sort(Comparator.comparing(ProductViewingDTO::getDateOfCreated)
                            .reversed());

        }
        return productList;
    }

    @Override
    public void addProductToDatabase(Product product,
                                     MultipartFile file1,
                                     MultipartFile file2) throws IOException {
        addImagesToProduct(product, file1, file2);
        Product productFromDb = productRepository.save(product);
        productFromDb.setPreviewImageId(productFromDb.getImages().get(0).getId());
        log.info("Saving new Product. Id: {}, Title: {} ", product.getId(), product.getTitle());
        productRepository.save(product);
    }

    @Override
    public void deleteProductById(Long id) {
        log.info("Delete product. Id: {}", id);
        bucketService.deleteProductByIdFromBucket(id);
        orderService.deleteProductFromOrdersById(id);
        productRepository.deleteById(id);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    private void addImagesToProduct(Product product,
                                    MultipartFile multipartPreviewFile,
                                    MultipartFile multipartFile) throws IOException {
        Image productPreviewImage;
        Image productImage;
        if (multipartPreviewFile.getSize() != 0) {
            if (!product.getImages().isEmpty()) {
                deleteProductImagesFromDatabase(product);
            }
            productPreviewImage = toImageEntity(multipartPreviewFile);
            productPreviewImage.setPreviewImage(true);
            product.addImageToProduct(productPreviewImage);
        }
        if (multipartFile.getSize() != 0) {
            productImage = toImageEntity(multipartFile);
            product.addImageToProduct(productImage);
        }
    }

    private void deleteProductImagesFromDatabase(Product product) {
        imageRepository.markToDeleteByProductId(product.getId(), "toDelete");
        imageRepository.deleteByName("toDelete");
        log.warn("Delete product images.");
        product.setPreviewImageId(null);
        product.getImages().clear();
    }

    @Override
    public void updateProductById(Long id,
                                  Product productUpdate,
                                  MultipartFile multipartPreviewFile,
                                  MultipartFile multipartFile) throws IOException {
        Product product = this.getProductById(id);

        addImagesToProduct(product, multipartPreviewFile, multipartFile);
        product.setTitle(productUpdate.getTitle());
        product.setDescription(productUpdate.getDescription());
        product.setPrice(productUpdate.getPrice());
        product.setWeight(productUpdate.getWeight());
        product.setAvailability(productUpdate.isAvailability());
        Product productFromDb = productRepository.save(product);
        product.setPreviewImageId(productFromDb.getImages().get(0).getId());
        log.info("Update product. Id: {}", id);
        productRepository.save(product);
    }

    @Override
    public void addProductIdToUserBucket(Long productId,
                                         String email) {
        User user = userService.getUserByEmail(email);
        if (user == null) {
            log.error("User not found. Email: " + email);
            throw new UserNotFoundException("User not found. Email: " + email);
        }

        Bucket bucket = user.getBucket();
        if (bucket == null) {
            Bucket newBucket = bucketService.createUserBucket(user, Collections.singletonList(productId));
            user.setBucket(newBucket);
            log.info("Create new bucket for user. User id: {}", user.getId());
            userService.save(user);
        } else {
            bucketService.addProductsToUserBucket(bucket, Collections.singletonList(productId));
        }
    }
}
