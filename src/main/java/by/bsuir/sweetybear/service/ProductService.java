package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.model.Product;
import by.bsuir.sweetybear.model.enums.SortType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Nov 2022
 */

public interface ProductService {

    // Return all products, and search by title
    List<Product> listProducts(String title, SortType type);
    // Save product with images
    void addProductToDatabase(Product product, MultipartFile multipartPreviewFile, MultipartFile multipartFile) throws IOException;
    // Delete product by id
    void deleteProductById(Long id);
    // Return product, which found by id
    Product getProductById(Long id);
    // Update product information
    void updateProductById(Long id, Product productUpdate, MultipartFile multipartPreviewFile, MultipartFile multipartFile) throws IOException;
    // Add product to bucket
    void addProductIdToUserBucket(Long productId, String email);

}
