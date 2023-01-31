package by.bsuir.sweetybear.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Nov 2022
 */

public record ApiException(String message,
                           Throwable throwable,
                           HttpStatus httpStatus,
                           ZonedDateTime timestamp
) {}
