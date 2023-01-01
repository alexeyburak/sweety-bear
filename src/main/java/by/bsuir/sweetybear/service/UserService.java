package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.model.Product;
import by.bsuir.sweetybear.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

public interface UserService {
    boolean addUserToDatabase(User user);
    void addUserAfterOauthLoginSuccess(String email, String name);
    void banUserAccountById(Long id);
    void updateUserDataById(Long id, User userUpdate, MultipartFile multipartFile) throws IOException;
    void updateUserPasswordId(Long id, User userUpdate);
    void changeUserRole(User user);
    void save(User user);
    boolean activateUserAccountAfterRegistration(String code);
    void deleteUserAccountById(Long id);
    void deleteUserAvatarById(Long id);
    void addProductToFavoritesAndRemoveIfExists(String authorizedEmail, Product product);
}
