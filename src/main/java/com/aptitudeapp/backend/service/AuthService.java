package com.aptitudeapp.backend.service;


import com.aptitudeapp.backend.model.User;
import com.aptitudeapp.backend.repository.UserRepository;
import com.aptitudeapp.backend.util.PhoneUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PhoneUtil phoneUtil;

    public User createOrGetUser(String phone) {

        String phoneHash = phoneUtil.hash(phone);

        return userRepository.findByPhoneHash(phoneHash)
                .orElseGet(() -> {

                    try {
                        User user = new User();
                        user.setPhoneHash(phoneHash);
                        user.setPhoneEncrypted(phoneUtil.encrypt(phone));
                        return userRepository.save(user);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
