package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.dto.BucketDTO;
import by.bsuir.sweetybear.dto.BucketDetailDTO;
import by.bsuir.sweetybear.model.*;
import by.bsuir.sweetybear.model.enums.OrderStatus;
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

import static by.bsuir.sweetybear.utils.Utils.removeOnlyOneIdFromList;

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
    private final OrderServiceImpl orderService;

    @Override
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

    @Override
    public void addProducts(Bucket bucket, List<Long> productIds) {
        List<Product> products = bucket.getProducts();
        List<Product> newProductList = products == null ? new ArrayList<>() : new ArrayList<>(products);
        newProductList.addAll(getCollectRefProductsByIds(productIds));
        bucket.setProducts(newProductList);
        log.info("Add products to bucket. Id: {}", bucket.getId());
        bucketRepository.save(bucket);
    }

    @Override
    public void deleteProductFromBucket(Bucket bucket, Long id) {
        List<Long> productIdsInBucket = bucket.getProducts().stream().map(Product::getId).toList();
        List<Long> productWithRemovedId = removeOnlyOneIdFromList(productIdsInBucket, id);
        bucket.setProducts(new ArrayList<>(getCollectRefProductsByIds(productWithRemovedId)));
        log.info("Delete product from bucket. Bucket id: {}. Product id: {}", bucket.getId(), id);
        bucketRepository.save(bucket);
    }

    @Override
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

    @Override
    @Transactional
    public void addBucketToOrder(String email) {
        User user = userService.getUserByEmail(email);
        if (user == null)
            throw new IllegalStateException("User is not found");
        Bucket bucket = user.getBucket();
        if (bucket == null || bucket.getProducts().isEmpty())
            throw new IllegalStateException("Bucket is empty");
        Order order = new Order();
        order.setStatus(OrderStatus.NEW);
        order.setUser(user);

        Map<Product, Long> productWithAmount = bucket.getProducts().stream()
                .collect(Collectors.groupingBy(product -> product, Collectors.counting()));

        List<OrderDetails> orderDetails = productWithAmount.entrySet().stream()
                .map(pair -> new OrderDetails(order, pair.getKey(), pair.getValue()))
                .toList();

        BigDecimal total = BigDecimal.valueOf(orderDetails.stream()
                .map(detail -> detail.getPrice().multiply(detail.getAmount()))
                .mapToDouble(BigDecimal::doubleValue).sum());

        order.setDetails(orderDetails);
        order.setSum(total);
        order.setAddress("test");
        log.info("Add bucket to order. Bucket id: {}. Order id: {}", bucket.getId(), order.getId());
        orderService.saveOrder(order);
        bucket.getProducts().clear();
        log.info("Clear bucket after adding to bucket. Bucket id: {}", bucket.getId());
        bucketRepository.save(bucket);
    }
}
