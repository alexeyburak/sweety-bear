package by.bsuir.sweetybear.service.mapper;

import by.bsuir.sweetybear.dto.user.UserDTO;
import by.bsuir.sweetybear.model.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

@Service
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .roles(user.getRoles())
                .email(user.getEmail())
                .name(user.getName())
                .active(user.isActive())
                .dateOfCreated(user.getDateOfCreated())
                .build();
    }
}
