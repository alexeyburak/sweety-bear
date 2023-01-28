package by.bsuir.sweetybear.dto.user;

import by.bsuir.sweetybear.model.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    Long id;
    String email;
    String name;
    boolean active;
    LocalDateTime dateOfCreated;
    Set<Role> roles = new HashSet<>();

    public boolean isOwner() {
        return roles.contains(Role.ROLE_OWNER);
    }
}
