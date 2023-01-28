package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.dto.bucket.BucketDTO;
import by.bsuir.sweetybear.model.Address;
import by.bsuir.sweetybear.model.Bucket;
import by.bsuir.sweetybear.model.User;

import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

public interface BucketService {
    Bucket createUserBucket(User user, List<Long> productIds);
    void addProductsToUserBucket(Bucket bucket, List<Long> productIds);
    void deleteProductFromBucket(Bucket bucket, Long id);
    BucketDTO getBucketByUser(String email);
    void addBucketToOrder(String email, Address address, boolean isDelivery);
    void deleteProductByIdFromBucket(Long id);
}
