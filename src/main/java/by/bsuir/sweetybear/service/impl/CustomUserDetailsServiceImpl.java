package by.bsuir.sweetybear.service.impl;

import by.bsuir.sweetybear.exception.ApiRequestException;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null)
            throw new UsernameNotFoundException("User not found");
        if (!user.isActive())
            throw new ApiRequestException("User is banned");

        return user;
    }
}
