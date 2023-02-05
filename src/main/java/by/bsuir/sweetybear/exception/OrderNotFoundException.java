package by.bsuir.sweetybear.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

@ResponseStatus(value = HttpStatus.NOT_FOUND)

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
