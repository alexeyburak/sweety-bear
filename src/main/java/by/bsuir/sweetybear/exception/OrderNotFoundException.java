package by.bsuir.sweetybear.exception;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
