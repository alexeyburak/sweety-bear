package by.bsuir.sweetybear.exception;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
