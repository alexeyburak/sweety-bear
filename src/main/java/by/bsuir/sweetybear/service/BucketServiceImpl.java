package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.dto.BucketDTO;
import by.bsuir.sweetybear.dto.BucketDetailDTO;
import by.bsuir.sweetybear.model.Bucket;
import by.bsuir.sweetybear.model.Product;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.repository.BucketRepository;
import by.bsuir.sweetybear.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static by.bsuir.sweetybear.utils.Utils.remove;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Nov 2022
 */


@Service
@Slf4j
@AllArgsConstructor
public class BucketServiceImpl implements BucketService {

    private final BucketRepository bucketRepository;
    private final ProductRepository productRepository;
    private final UserServiceImpl userService;

    @Transactional
    public Bucket createBucket(User user, List<Long> productIds) {
        Bucket bucket = new Bucket();
        bucket.setUser(user);
        log.info("Create bucket. User: {}", user.getEmail());
        List<Product> productList = getCollectRefProductsByIds(productIds);
        bucket.setProducts(productList);
        return bucketRepository.save(bucket);
    }

    private List<Product> getCollectRefProductsByIds(List<Long> productIds) {
        return productIds.stream().map(productRepository::getOne).collect(Collectors.toList());
    }

    public void addProducts(Bucket bucket, List<Long> productIds) {
        List<Product> products = bucket.getProducts();
        List<Product> newProductList = products == null ? new ArrayList<>() : new ArrayList<>(products);
        newProductList.addAll(getCollectRefProductsByIds(productIds));
        bucket.setProducts(newProductList);
        bucketRepository.save(bucket);
    }

    public void deleteProductFromBucket(Bucket bucket, Long id) {
        List<Long> productIdsInBucket = bucket.getProducts().stream().map(Product::getId).toList();
        List<Long> productWithRemovedId = remove(productIdsInBucket, id);
        bucket.setProducts(new ArrayList<>(getCollectRefProductsByIds(productWithRemovedId)));
        bucketRepository.save(bucket);
    }

    public BucketDTO getBucketByUser(String email) {
        User user = userService.getUserByEmail(email);
        if (user == null || user.getBucket() == null) {
            return new BucketDTO();
        }
        BucketDTO bucketDTO = new BucketDTO();
        Map<Long, BucketDetailDTO> mapByProductId = new HashMap<>();

        List<Product> products = user.getBucket().getProducts();
        for (Product product : products) {
            BucketDetailDTO detail = mapByProductId.get(product.getId());
            if (detail == null) {
                mapByProductId.put(product.getId(), new BucketDetailDTO(product));
            } else {
                detail.setAmount(detail.getAmount().add(new BigDecimal("1.0")));
                detail.setSum(detail.getSum() + Double.parseDouble(product.getPrice().toString()));
            }
        }

        bucketDTO.setBucketDetails(new ArrayList<>(mapByProductId.values()));
        bucketDTO.aggregate();
        return bucketDTO;
    }
}
