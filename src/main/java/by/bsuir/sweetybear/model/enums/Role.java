package by.bsuir.sweetybear.model.enums;

import org.springframework.security.core.GrantedAuthority;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

public enum Role implements GrantedAuthority {
    ROLE_USER, ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
