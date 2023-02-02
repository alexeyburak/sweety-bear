package by.bsuir.sweetybear.service.impl;

import by.bsuir.sweetybear.model.Product;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.model.enums.Role;
import by.bsuir.sweetybear.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void banUserById() {
        User user = User.builder()
                .active(true)
                .build();

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Assertions.assertTrue(user.isActive());
        userService.banUserAccountById(1L);

        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Assertions.assertNotNull(user);
        Assertions.assertFalse(user.isActive());
    }

    @Test
    public void changeUserRole() {
        User user = User.builder()
                .roles(new HashSet<>(Collections.singletonList(Role.ROLE_USER)))
                .build();

        Assertions.assertEquals(1, user.getRoles().size());
        Assertions.assertEquals(Set.of(Role.ROLE_USER), user.getRoles());

        userService.changeUserRole(user);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(1, user.getRoles().size());
        Assertions.assertEquals(Set.of(Role.ROLE_ADMIN), user.getRoles());
    }

    @Test
    public void activateUserAccount() {
        String activationCode = UUID.randomUUID().toString();
        User user = User.builder()
                .active(false)
                .activationCode(activationCode)
                .build();

        Mockito.when(userRepository.findByActivationCode(activationCode)).thenReturn(user);

        Assertions.assertFalse(user.isActive());
        Assertions.assertEquals(activationCode, user.getActivationCode());

        userService.activateUserAccountAfterRegistration(activationCode);

        Mockito.verify(userRepository, Mockito.times(1)).findByActivationCode(activationCode);
        Assertions.assertNotNull(user);
        Assertions.assertTrue(user.isActive());
        Assertions.assertNull(user.getActivationCode());
    }

    @Test
    public void addProductToFavoritesAndRemoveIfExistsTest() {
        Product firstProduct = createProduct(1L);
        Product secondProduct = createProduct(2L);
        Product thirdProduct = createProduct(3L);
        String email = "burakalexey@yahoo.com";
        User user = User.builder()
                .email(email)
                .favoriteProducts(new ArrayList<>(
                        List.of(
                                firstProduct,
                                secondProduct
                        )
                ))
                .build();

        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);

        Assertions.assertNotNull(user.getFavoriteProducts());
        Assertions.assertEquals(2, user.getFavoriteProducts().size());

        userService.addProductToFavoritesAndRemoveIfExists(email, firstProduct);

        Assertions.assertNotNull(user.getFavoriteProducts());
        Assertions.assertEquals(1, user.getFavoriteProducts().size());
        Assertions.assertEquals(2L, user.getFavoriteProducts().get(0).getId());

        userService.addProductToFavoritesAndRemoveIfExists(email, thirdProduct);

        Assertions.assertNotNull(user.getFavoriteProducts());
        Assertions.assertEquals(2, user.getFavoriteProducts().size());
        Assertions.assertEquals(3L, user.getFavoriteProducts().get(1).getId());
    }

    private Product createProduct(Long id) {
        return Product.builder()
                .id(id)
                .build();
    }

    @Test
    public void updateUserPasswordById() {
        final String password = "password";
        final String newPassword = "newPassword";
        User user = User.builder()
                .password(password)
                .build();
        User updatedUser = User.builder()
                .password(newPassword)
                .build();
        Mockito.when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Assertions.assertNotNull(user);
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(password, user.getPassword());

        userService.updateUserPasswordById(1L, updatedUser);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(newPassword, user.getPassword());
    }

}
