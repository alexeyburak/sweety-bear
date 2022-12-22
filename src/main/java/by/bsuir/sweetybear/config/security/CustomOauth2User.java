package by.bsuir.sweetybear.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

@RequiredArgsConstructor
public class CustomOauth2User implements OAuth2User {

    private final OAuth2User oauth2User;

    @Override
    public String getName() {
        return oauth2User.getAttribute("given_name");
    }

    public String getEmail() {
        return oauth2User.getAttribute("email");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }
}
