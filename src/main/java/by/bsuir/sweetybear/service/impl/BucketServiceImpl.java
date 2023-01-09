package by.bsuir.sweetybear.service.impl;

import by.bsuir.sweetybear.dto.BucketDTO;
import by.bsuir.sweetybear.dto.BucketDetailDTO;
import by.bsuir.sweetybear.exception.ApiRequestException;
import by.bsuir.sweetybear.model.*;
import by.bsuir.sweetybear.model.enums.DeliveryType;
import by.bsuir.sweetybear.model.enums.OrderStatus;
import by.bsuir.sweetybear.repository.BucketRepository;
import by.bsuir.sweetybear.repository.ProductRepository;
import by.bsuir.sweetybear.service.BucketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static by.bsuir.sweetybear.utils.Utils.removeOnlyOneIdFromList;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
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
    public Bucket createUserBucket(final User user,
                                   List<Long> productIds) {
        List<Product> productList = getCollectRefProductsByIds(productIds);
        log.info("Create bucket. User: {}", user.getEmail());
        return bucketRepository.save(
                Bucket.builder()
                        .user(user)
                        .products(productList)
                        .build()
        );
    }

    @Override
    public void addProductsToUserBucket(Bucket bucket,
                                        List<Long> productIds) {
        List<Product> products = getProductsFromBucket(bucket);
        List<Product> newProductList = products == null ? new ArrayList<>() : new ArrayList<>(products);
        newProductList.addAll(getCollectRefProductsByIds(productIds));
        bucket.setProducts(newProductList);
        log.info("Add products to bucket. Id: {}", bucket.getId());
        bucketRepository.save(bucket);
    }

    private List<Product> getCollectRefProductsByIds(List<Long> productIds) {
        return productIds
                .stream()
                .map(productRepository::getOne)
                .collect(Collectors.toList());
    }

    private List<Product> getProductsFromBucket(Bucket bucket) {
        return bucket.getProducts();
    }

    @Override
    public void deleteProductFromBucket(Bucket bucket,
                                        Long id) {
        List<Long> productIdsInBucket = getProductsFromBucket(bucket)
                .stream()
                .map(Product::getId)
                .toList();
        List<Long> productWithRemovedId = removeOnlyOneIdFromList(productIdsInBucket, id);
        bucket.setProducts(new ArrayList<>(getCollectRefProductsByIds(productWithRemovedId)));
        log.info("Delete product from bucket. Bucket id: {}. Product id: {}", bucket.getId(), id);
        bucketRepository.save(bucket);
    }

    @Override
    public BucketDTO getBucketByUser(final String email) {
        User user = userService.getUserByEmail(email);
        if (user == null || user.getBucket() == null) {
            return new BucketDTO();
        }
        BucketDTO bucketDTO = new BucketDTO();
        Map<Long, BucketDetailDTO> mapByProductId = new HashMap<>();

        List<Product> products = user.getBucket().getProducts();

        completionOfBucketDetails(mapByProductId, products);

        bucketDTO.setBucketDetails(new ArrayList<>(mapByProductId.values()));
        bucketDTO.aggregate();
        return bucketDTO;
    }

    private void completionOfBucketDetails(Map<Long, BucketDetailDTO> mapByProductId,
                                           List<Product> products) {
        for (Product product : products) {
            BucketDetailDTO detail = mapByProductId.get(product.getId());
            if (detail == null) {
                mapByProductId.put(product.getId(), new BucketDetailDTO(product));
            } else {
                detail.setAmount(detail.getAmount().add(new BigDecimal("1.0")));
                detail.setSum(detail.getSum() + Double.parseDouble(product.getPrice().toString()));
            }
        }
    }

    @Override
    @Transactional
    public void addBucketToOrder(final String email,
                                 final Address address) {
        User user = this.getUserForOrderAndSetAddress(email, address);

        Bucket bucket = user.getBucket();
        if (bucket == null || getProductsFromBucket(bucket).isEmpty()) {
            throw new ApiRequestException("Bucket is empty");
        }
        Order order = new Order();
        order.setStatus(OrderStatus.NEW);
        order.setUser(user);

        Map<Product, Long> productsWithAmount = getProductsWithAmountsFromBucket(bucket);

        List<OrderDetails> orderDetails = fillOrderDetailsToOrder(productsWithAmount, order);

        BigDecimal totalSum = countOrderTotalSum(orderDetails);
        order.setSum(totalSum);

        addDeliveryStatusAndAddressToOrder(order, address);
        order.setDetails(orderDetails);
        log.info("Add bucket to order. Bucket id: {}.", bucket.getId());
        orderService.save(order);

        clearProductsFromBucket(bucket);
    }

    private void addDeliveryStatusAndAddressToOrder(final Order order,
                                                    final Address address) {
        if (address.getStreet().isEmpty())
            order.setDelivery(DeliveryType.PICKUP);
        else {
            order.setAddress(address);
            order.setDelivery(DeliveryType.DELIVERY);
        }
    }

    private User getUserForOrderAndSetAddress(final String email,
                                              final Address address) {
        User user = userService.getUserByEmail(email);
        if (user == null)
            throw new ApiRequestException("User is not found");
        if (!address.getStreet().isEmpty())
            user.setAddress(address);

        return user;
    }

    private void clearProductsFromBucket(Bucket bucket) {
        getProductsFromBucket(bucket).clear();
        log.info("Clear bucket after adding to order. Bucket id: {}", bucket.getId());
        bucketRepository.save(bucket);
    }

    private Map<Product, Long> getProductsWithAmountsFromBucket(Bucket bucket) {
        return getProductsFromBucket(bucket).stream()
                .collect(Collectors.groupingBy(product -> product, Collectors.counting()));
    }

    private List<OrderDetails> fillOrderDetailsToOrder(Map<Product, Long> productWithAmount,
                                                       Order order) {
        return productWithAmount.entrySet()
                .stream()
                .map(pair -> new OrderDetails(order, pair.getKey(), pair.getValue()))
                .toList();
    }

    private BigDecimal countOrderTotalSum(List<OrderDetails> orderDetails) {
        return BigDecimal.valueOf(orderDetails.stream()
                .map(detail -> detail.getPrice().multiply(detail.getAmount()))
                .mapToDouble(BigDecimal::doubleValue).sum());
    }

    @Override
    public void deleteProductByIdFromBucket(Long id) {
        log.warn("Delete product from bucket. Product id: {}", id);
        bucketRepository.deleteByProductId(id);
    }
}
