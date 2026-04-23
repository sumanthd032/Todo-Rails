package com.todorails.service;

import com.todorails.model.User;
import com.todorails.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Spring Security calls this automatically during login
    // It passes whatever the user typed in the username field
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        // find user in our DB
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + username)
                );

        // return Spring's UserDetails object
        // Spring will then compare the password using BCrypt automatically
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()   // roles/authorities (we'll keep it empty for now)
        );
    }
}