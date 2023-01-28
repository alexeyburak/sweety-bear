package by.bsuir.sweetybear.service.mapper;

import by.bsuir.sweetybear.dto.product.ProductViewingDTO;
import by.bsuir.sweetybear.model.Product;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

@Service
public class ProductDTOMapper implements Function<Product, ProductViewingDTO> {
    @Override
    public ProductViewingDTO apply(Product product) {
        return ProductViewingDTO.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .weight(product.getWeight())
                .availability(product.isAvailability())
                .images(product.getImages())
                .previewImageId(product.getPreviewImageId())
                .dateOfCreated(product.getDateOfCreated())
                .build();
    }
}
