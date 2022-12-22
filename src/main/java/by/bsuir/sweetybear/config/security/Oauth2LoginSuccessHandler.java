package by.bsuir.sweetybear.config.security;

import by.bsuir.sweetybear.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

@Component
@RequiredArgsConstructor
public class Oauth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserServiceImpl userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOauth2User oauth2User = (CustomOauth2User) authentication.getPrincipal();
        this.setDefaultTargetUrl("/login");

        String email = oauth2User.getEmail();
        if (userService.getUserByEmail(email) == null) {
            this.setDefaultTargetUrl("/login?oauth2_login");
            userService.addUserAfterOauthLoginSuccess(email, oauth2User.getName());
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

}
