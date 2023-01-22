package by.bsuir.sweetybear.validator;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

public interface ErrorMessage {
    String PASSWORDS_NOT_MATCH = "Passwords do not match.";
    String USER_INVALID_DATA_INPUT = "Invalid username or password.";
    String USER_INVALID_PASSWORD_INPUT = "Invalid password.";
    String USER_INVALID_USERNAME_INPUT = "Invalid username.";
    String USER_ACCOUNT_BANNED = "Your account was banned.";
    String USER_CONFIRM_EMAIL = "You successfully confirm your email.";
    String USER_INVALID_ACTIVATION_CODE = "Activation code is not available.";
    String USER_EMAIL_EXISTS = "Email is already exists.";
    String USER_NAME_LENGTH_ERROR = "Name must be between 2 and 15 characters.";
    String ORDER_PAYMENT_ERROR = "Payment error. Please, try again.";
    String RESET_PASSWORD_NOT_FOUND_ACCOUNT = "Your account was not found.";
    String RESET_PASSWORD_CHECK_EMAIL = "Check your email for further password recovery.";
    String RESET_PASSWORD_SUCCESS = "You successfully changed your password.";
    String RESET_PASSWORD_ERROR = "Error changing password. Please, try again.";
    String ADD_BANK_CARD_ERROR = "Error adding card. Please check input data.";
    String ADDRESS_INVALID_DATA_INPUT = "Invalid address data.";
}
