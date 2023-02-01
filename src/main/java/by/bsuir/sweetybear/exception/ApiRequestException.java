package by.bsuir.sweetybear.exception;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Nov 2022
 */

public class ApiRequestException extends RuntimeException {
    public ApiRequestException(String message) {
        super(message);
    }
}
