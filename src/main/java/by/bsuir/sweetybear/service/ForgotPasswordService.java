package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.model.User;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

public interface ForgotPasswordService {

    boolean setCodeToResetUserPassword(String email);
    boolean changeUserPassword(String resetPasswordCode, User userUpdate);
}
