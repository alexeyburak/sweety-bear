package by.bsuir.sweetybear.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

@ResponseStatus(HttpStatus.NO_CONTENT)

public class BucketNullException extends RuntimeException {
    public BucketNullException(String message) {
        super(message);
    }
}
