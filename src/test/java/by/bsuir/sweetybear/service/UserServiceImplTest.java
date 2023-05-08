package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.model.Product;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.model.enums.Role;
import by.bsuir.sweetybear.repository.UserRepository;
import by.bsuir.sweetybear.service.impl.MailSenderImpl;
import by.bsuir.sweetybear.service.impl.UserServiceImpl;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static by.bsuir.sweetybear.utils.UUIDGenerator.generateUUID;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private MailSenderImpl mailSender;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void addUserToDatabase_NotExistsUser_True() {
        // given
        String code = generateUUID();
        String email = "burakalexey@yahoo.com";
        User user = User.builder()
                .email(email)
                .active(false)
                .password(code)
                .roles(new HashSet<>(Collections.singletonList(Role.ROLE_USER)))
                .activationCode(code)
                .build();

        //when
        Mockito.when(userRepository.findByEmail(email)).thenReturn(null);
        Mockito.when(passwordEncoder.encode(code)).thenReturn(code);
        doNothing().when(mailSender).sendEmailWithActivationLinkToUser(user);
        boolean result = userService.addUserToDatabase(user);

        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository, Mockito.times(1)).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedUser);
        AssertionsForClassTypes.assertThat(capturedUser).isEqualTo(user);
        Assertions.assertTrue(result);
    }

    @Test
    void getUserByResetPasswordCode_UserWithResetCode_ShouldReturnUserMatchesResetCode() {
        //given
        String resetCode = generateUUID();
        User user = User.builder()
                .resetPasswordCode(resetCode)
                .build();

        //when
        Mockito.when(userRepository.findByResetPasswordCode(resetCode)).thenReturn(user);
        User userFromDatabase = userService.getUserByResetPasswordCode(resetCode);

        //then
        Assertions.assertNotNull(userFromDatabase);
        Assertions.assertEquals(user, userFromDatabase);
    }

    @Test
    void banUserAccountById_ActiveUser_ShouldChangeUserActivity() {
        //given
        User user = User.builder()
                .active(true)
                .build();

        //when
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        userService.banUserAccountById(1L);

        //then
        Assertions.assertNotNull(user);
        Assertions.assertFalse(user.isActive());
    }

    @Test
    void changeUserRole_UserRoleUser_ShouldChangeForRoleAdmin() {
        //given
        User user = User.builder()
                .roles(new HashSet<>(Collections.singletonList(Role.ROLE_USER)))
                .build();

        //when
        userService.changeUserRole(user);

        //then
        Assertions.assertNotNull(user);
        Assertions.assertEquals(1, user.getRoles().size());
        Assertions.assertEquals(Set.of(Role.ROLE_ADMIN), user.getRoles());
    }

    @Test
    void changeUserRole_UserRoleAdmin_ShouldChangeForRoleUser() {
        //given
        User user = User.builder()
                .roles(new HashSet<>(Collections.singletonList(Role.ROLE_ADMIN)))
                .build();

        //when
        userService.changeUserRole(user);

        //then
        Assertions.assertNotNull(user);
        Assertions.assertEquals(1, user.getRoles().size());
        Assertions.assertEquals(Set.of(Role.ROLE_USER), user.getRoles());
    }

    @Test
    void activateUserAccountAfterRegistration_FindUserByActivationCode_ShouldChangeUserActivitySetCodeNullAndReturnTrue() {
        //given
        String activationCode = generateUUID();
        User user = User.builder()
                .active(false)
                .activationCode(activationCode)
                .build();

        //when
        Mockito.when(userRepository.findByActivationCode(activationCode)).thenReturn(user);
        userService.activateUserAccountAfterRegistration(activationCode);

        //then
        Assertions.assertNotNull(user);
        Assertions.assertTrue(user.isActive());
        Assertions.assertNull(user.getActivationCode());
    }

    @Test
    void addProductToFavoritesAndRemoveIfExists_ExistsProduct_ShouldRemoveFromList() {
        //given
        Product firstProduct = createProduct(1L);
        Product secondProduct = createProduct(2L);
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


        //when
        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);
        userService.addProductToFavoritesAndRemoveIfExists(email, firstProduct);

        //then
        Assertions.assertNotNull(user.getFavoriteProducts());
        Assertions.assertEquals(1, user.getFavoriteProducts().size());
        Assertions.assertEquals(2L, user.getFavoriteProducts().get(0).getId());
    }

    @Test
    void addProductToFavoritesAndRemoveIfExists_NotExistsProduct_ShouldAddProductToList() {
        //given
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


        //when
        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);
        userService.addProductToFavoritesAndRemoveIfExists(email, thirdProduct);

        //then
        Assertions.assertNotNull(user.getFavoriteProducts());
        Assertions.assertEquals(3, user.getFavoriteProducts().size());
        Assertions.assertEquals(1L, user.getFavoriteProducts().get(0).getId());
    }

    private Product createProduct(Long id) {
        return Product.builder()
                .id(id)
                .build();
    }

    @Test
    void updateUserPasswordById_UpdatedUserAndIdEditedUser_ShouldChangeUserPassword() {
        //given
        final String password = "password";
        final String newPassword = "newPassword";
        User user = User.builder()
                .password(password)
                .build();
        User updatedUser = User.builder()
                .password(newPassword)
                .build();

        //when
        Mockito.when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        userService.updateUserPasswordById(1L, updatedUser);

        //then
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getPassword());
        Assertions.assertEquals(newPassword, user.getPassword());
    }
}
