package by.bsuir.sweetybear.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Nov 2022
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)

public class ApiRequestException extends RuntimeException {
    public ApiRequestException(String message) {
        super(message);
    }
}
