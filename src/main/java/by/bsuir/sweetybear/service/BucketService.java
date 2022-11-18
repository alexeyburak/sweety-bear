package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.dto.BucketDTO;
import by.bsuir.sweetybear.model.Bucket;
import by.bsuir.sweetybear.model.User;

import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Nov 2022
 */

public interface BucketService {

    // Create bucket
    Bucket createUserBucket(User user, List<Long> productIds);
    // Add products to user bucket
    void addProductsToUserBucket(Bucket bucket, List<Long> productIds);
    // Delete one product from bucket
    void deleteProductFromBucket(Bucket bucket, Long id);
    // Count bucket price
    BucketDTO getBucketByUser(String email);
    // Add bucket to order
    void addBucketToOrder(String email, String address);
    // Delete product from database
    void deleteProductByIdFromBucket(Long id);
}
