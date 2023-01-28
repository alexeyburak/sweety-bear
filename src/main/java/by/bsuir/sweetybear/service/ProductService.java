package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.dto.product.ProductViewingDTO;
import by.bsuir.sweetybear.model.Product;
import by.bsuir.sweetybear.model.enums.ProductSortType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

public interface ProductService {
    List<ProductViewingDTO> listProducts(String title, ProductSortType type);
    void addProductToDatabase(Product product, MultipartFile multipartPreviewFile, MultipartFile multipartFile) throws IOException;
    void deleteProductById(Long id);
    Product getProductById(Long id);
    void updateProductById(Long id, Product productUpdate, MultipartFile multipartPreviewFile, MultipartFile multipartFile) throws IOException;
    void addProductIdToUserBucket(Long productId, String email);
}
