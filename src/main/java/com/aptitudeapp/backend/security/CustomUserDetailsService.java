package com.aptitudeapp.backend.security;

import com.aptitudeapp.backend.model.User;
import com.aptitudeapp.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId)
            throws UsernameNotFoundException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getId())
                .password("") // Not needed for OTP
                .authorities(user.getRole().name())
                .build();
    }
}
