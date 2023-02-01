package by.bsuir.sweetybear.service.impl;

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

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
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

}
