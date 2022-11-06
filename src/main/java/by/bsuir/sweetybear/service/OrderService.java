package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.model.Order;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Nov 2022
 */

public interface OrderService {

    // Save order to database
    void saveOrder(Order order);
}
