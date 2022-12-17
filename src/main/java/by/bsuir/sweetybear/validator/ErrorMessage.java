package by.bsuir.sweetybear.validator;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

public class ErrorMessage {
    public static final String PASSWORDS_NOT_MATCH = "Passwords do not match.";
    public static final String USER_INVALID_DATA_INPUT = "Invalid username or password.";
    public static final String USER_INVALID_PASSWORD_INPUT = "Invalid password.";
    public static final String USER_INVALID_USERNAME_INPUT = "Invalid username.";
    public static final String USER_ACCOUNT_BANNED = "Your account was banned.";
    public static final String USER_CONFIRM_EMAIL = "You successfully confirm your email.";
    public static final String USER_INVALID_ACTIVATION_CODE = "Activation code is not available.";
    public static final String USER_EMAIL_EXISTS = "Email is already exists.";
    public static final String USER_NAME_LENGTH_ERROR = "Name must be between 2 and 15 characters.";
    public static final String ORDER_PAYMENT_ERROR = "Payment error. Check the entered data or the card balance.";
    public static final String RESET_PASSWORD_NOT_FOUND_ACCOUNT = "Your account was not found.";
    public static final String RESET_PASSWORD_CHECK_EMAIL = "Check your email for further password recovery.";
    public static final String RESET_PASSWORD_SUCCESS = "You successfully changed your password.";
    public static final String RESET_PASSWORD_ERROR = "Error changing password. Please, try again.";
}
