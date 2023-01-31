package by.bsuir.sweetybear.exception;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
